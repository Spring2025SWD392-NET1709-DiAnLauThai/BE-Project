package com.be.back_end.service.StatisticService;

import com.be.back_end.dto.response.*;
import com.be.back_end.model.Bookings;
import com.be.back_end.model.Transaction;
import com.be.back_end.model.Tshirts;
import com.be.back_end.repository.BookingDetailsRepository;
import com.be.back_end.repository.BookingRepository;
import com.be.back_end.repository.TransactionRepository;
import com.be.back_end.repository.TshirtsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticService implements IStatisticService {

    private final TransactionRepository transactionRepository;
    private final BookingRepository bookingRepository;
    private final BookingDetailsRepository bookingDetailsRepository;
    private final TshirtsRepository tshirtsRepository;

    @Autowired
    public StatisticService(TransactionRepository transactionRepository, BookingRepository bookingRepository, BookingDetailsRepository bookingDetailsRepository, TshirtsRepository tshirtsRepository) {
        this.transactionRepository = transactionRepository;
        this.bookingRepository = bookingRepository;
        this.bookingDetailsRepository = bookingDetailsRepository;
        this.tshirtsRepository = tshirtsRepository;
    }


    public DashboardStatResponse GetDashboardStatistics(LocalDate startDate, LocalDate endDate, int year) {

        // Convert to LocalDateTime for database query. This is the range
        // Convert to LocalDateTime for database query
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        //Get the total amount of T-Shirt created in range, group by Month
        List<Tshirts> tshirtList = tshirtsRepository.findTshirtsCreatedBetween(startDateTime, endDateTime);
        List<MonthyAmountResponse> tShirtCreatedAmount = calculateMonthlyAmountForTshirts(tshirtList);


        //Get the total amount of bookings created in range, group by Month
        List<Bookings> bookingList = bookingRepository.findBookingsCreatedBetween(startDateTime, endDateTime);
        List<MonthyAmountResponse> bookingCreatedAmount = calculateMonthlyAmountForBookings(bookingList);


        //Get the total amount of bookings completed in range, group by Month
        List<Bookings> completedBookings = bookingRepository.findBookingsCompletedBetween(startDateTime, endDateTime);
        List<MonthyAmountResponse> bookingCompletedAmount = calculateMonthlyAmountForBookings(completedBookings);

        //Get the total revenue of all transactions in range, group by Month
        List<MonthlyIncomeSummary> monthlyIncome = calculateMonthlyIncomeBasedOnDateRange(startDateTime, endDateTime);

        return createDashBoardStatResponse(year, startDate, endDate, monthlyIncome, tShirtCreatedAmount, bookingCreatedAmount, bookingCompletedAmount);

    }

    public List<MonthlyIncomeSummary> calculateMonthlyIncomeBasedOnDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime){

        //Including start date and end date
        List<Transaction> fullyPaidTransactions = transactionRepository
                .findFullyPaidTransactionsByDateRange(startDateTime, endDateTime);

        // Group transactions by month and calculate monthly totals
        Map<Month, BigDecimal> monthlyTotals = fullyPaidTransactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getTransactionDate().getMonth(),
                        Collectors.mapping(
                                Transaction::getTransactionAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));

        // Create a list of monthly income summaries for all months (including those with zero income)
        List<MonthlyIncomeSummary> monthlyIncomes = new ArrayList<>();
        for (Month month : Month.values()) {
            BigDecimal monthlyIncome = monthlyTotals.getOrDefault(month, BigDecimal.ZERO);
            monthlyIncomes.add(new MonthlyIncomeSummary(month, monthlyIncome));
        }

        // Sort by month
        monthlyIncomes.sort(Comparator.comparing(MonthlyIncomeSummary::getMonth));

        return  monthlyIncomes;
    }

    //Calculate total income of all transaction given date
    //format is 2025-03-18 (YYYY-MM-DD) ISO format
    @Override
    public BigDecimal calculateTotalIncomeByDate(LocalDate date) {

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);


        List<Transaction> fullyPaidTransactions = transactionRepository.findFullyPaidTransactionsByDateRange(startOfDay, endOfDay);


        return fullyPaidTransactions.stream()
                .map(Transaction::getTransactionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public MonthlyIncomeResponse calculateMonthlyIncome(int year, Month month) {
        // Get the first and last day of the month
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

        // Convert to LocalDateTime for database query
        LocalDateTime startDateTime = firstDayOfMonth.atStartOfDay();
        LocalDateTime endDateTime = lastDayOfMonth.plusDays(1).atStartOfDay();

        // Get all fully paid transactions within the month
        List<Transaction> fullyPaidTransactions = transactionRepository
                .findFullyPaidTransactionsByDateRange(startDateTime, endDateTime);

        // Calculate total income for the month
        BigDecimal totalIncome = fullyPaidTransactions.stream()
                .map(Transaction::getTransactionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Group transactions by date and calculate daily totals
        Map<LocalDate, BigDecimal> dailyTotalsMap = fullyPaidTransactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getTransactionDate().toLocalDate(),
                        Collectors.mapping(
                                Transaction::getTransactionAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));

        // Create a list with all days of the month (including those with zero income)
        List<DailyIncomeResponse> dailyTransactions = new ArrayList<>();

        // Initialize all days with zero
        for (int day = 1; day <= lastDayOfMonth.getDayOfMonth(); day++) {
            LocalDate currentDate = LocalDate.of(year, month, day);
            BigDecimal dailyAmount = dailyTotalsMap.getOrDefault(currentDate, BigDecimal.ZERO);
            dailyTransactions.add(new DailyIncomeResponse (currentDate, dailyAmount));
        }

        // Create and return the response
        return new MonthlyIncomeResponse(month, year, totalIncome, dailyTransactions);
    }

    @Override
    public YearlyIncomeResponse calculateYearlyIncome(int year) {
        // Calculate start and end of the year
        LocalDateTime startOfYear = LocalDate.of(year, 1, 1).atStartOfDay();
        LocalDateTime endOfYear = LocalDate.of(year, 12, 31).plusDays(1).atStartOfDay();

        // Get all fully paid transactions within the year
        List<Transaction> fullyPaidTransactions = transactionRepository
                .findFullyPaidTransactionsByDateRange(startOfYear, endOfYear);

        // Calculate total income for the year
        BigDecimal totalYearlyIncome = fullyPaidTransactions.stream()
                .map(Transaction::getTransactionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Group transactions by month and calculate monthly totals
        Map<Month, BigDecimal> monthlyTotals = fullyPaidTransactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getTransactionDate().getMonth(),
                        Collectors.mapping(
                                Transaction::getTransactionAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));

        // Create a list of monthly income summaries for all months (including those with zero income)
        List<MonthlyIncomeSummary> monthlyIncomes = new ArrayList<>();
        for (Month month : Month.values()) {
            BigDecimal monthlyIncome = monthlyTotals.getOrDefault(month, BigDecimal.ZERO);
            monthlyIncomes.add(new MonthlyIncomeSummary(month, monthlyIncome));
        }

        // Sort by month
        monthlyIncomes.sort(Comparator.comparing(MonthlyIncomeSummary::getMonth));

        // Create and return the response
        return new YearlyIncomeResponse(year, totalYearlyIncome, monthlyIncomes);
    }

    //Helper method to calculate monthly amounts for T-shirts
    private List<MonthyAmountResponse> calculateMonthlyAmountForTshirts(List<Tshirts> tshirtList) {
        // Group T-shirts by month
        Map<Month, Long> monthlyTshirtCounts = tshirtList.stream()
                .collect(Collectors.groupingBy(
                        tshirt -> tshirt.getCreatedAt().getMonth(),
                        Collectors.counting()
                ));

        // Create a list of monthly T-shirt amounts
        List<MonthyAmountResponse> monthlyAmounts = new ArrayList<>();
        for (Month month : Month.values()) {
            long count = monthlyTshirtCounts.getOrDefault(month, 0L);
            monthlyAmounts.add(new MonthyAmountResponse(month, count));
        }

        // Sort by month
        monthlyAmounts.sort(Comparator.comparing(MonthyAmountResponse::getMonth));

        return monthlyAmounts;
    }

    //Helper method to calculate monthly amounts for bookings
    private List<MonthyAmountResponse> calculateMonthlyAmountForBookings(List<Bookings> bookingList) {
        // Group bookings by month
        Map<Month, Long> monthlyBookingCounts = bookingList.stream()
                .collect(Collectors.groupingBy(
                        booking -> booking.getDatecreated().getMonth(),
                        Collectors.counting()
                ));

        // Create a list of monthly booking amounts
        List<MonthyAmountResponse> monthlyAmounts = new ArrayList<>();
        for (Month month : Month.values()) {
            long count = monthlyBookingCounts.getOrDefault(month, 0L);
            monthlyAmounts.add(new MonthyAmountResponse(month, count));
        }

        // Sort by month
        monthlyAmounts.sort(Comparator.comparing(MonthyAmountResponse::getMonth));

        return monthlyAmounts;
    }

    private DashboardStatResponse createDashBoardStatResponse(
            int year, LocalDate startDate, LocalDate endDate,
            List<MonthlyIncomeSummary> monthlyIncome, List<MonthyAmountResponse> tShirtCreatedAmount,
            List<MonthyAmountResponse> bookingCreatedAmount, List<MonthyAmountResponse> bookingCompletedAmount
    ){
        DashboardStatResponse response = new DashboardStatResponse();
        response.setYear(year);
        response.setStartDate(startDate);
        response.setEndDate(endDate);
        response.setMonthlyIncome(monthlyIncome);
        response.setTShirtCreatedAmount(tShirtCreatedAmount);
        response.setBookingCreatedAmount(bookingCreatedAmount);
        response.setBookingCompletedAmount(bookingCompletedAmount);
        return response;
    }
}
