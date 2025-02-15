package com.be.back_end.service.AccountService;


import com.be.back_end.dto.AccountDTO;
import com.be.back_end.enums.AccountEnums;
import com.be.back_end.enums.RoleEnums;
import com.be.back_end.model.Account;
import com.be.back_end.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService implements IAccountService{
    /*private final IGenericService<Account, UUID> genericService;*/

    private final AccountRepository accountRepository;

    public AccountService( AccountRepository accountRepository) {

        this.accountRepository = accountRepository;
    }
    private AccountDTO mapToDTO(Account account) {
        AccountDTO dto = new AccountDTO();
        
        dto.setEmail(account.getEmail());
        dto.setName(account.getName());
        dto.setAddress(account.getAddress());
        dto.setPhone(account.getPhone());

        dto.setDateOfBirth(account.getDateOfBirth());
        return dto;
    }

    private Account mapToEntity(AccountDTO dto) {
        Account account = new Account();

        account.setEmail(dto.getEmail());
        account.setName(dto.getName());
        account.setAddress(dto.getAddress());
        account.setPhone(dto.getPhone());
        account.setRole(RoleEnums.ADMIN);
        account.setDateOfBirth(dto.getDateOfBirth());
        account.setStatus(AccountEnums.ACTIVE);
        return account;
    }
    @Override
    public AccountDTO createUser(AccountDTO user) {
        Account newaccount= mapToEntity(user);
        accountRepository.save(newaccount);
        return user;
    }
    @Override
    public List<AccountDTO> getAllUsers() {
        List<Account> accounts= accountRepository.findAll();
        List<AccountDTO> list= new ArrayList<>();
        for(Account acc:accounts)
        {
            list.add(mapToDTO(acc));
            System.out.println(acc.getId());
        }
        return list;
    }
    @Override
    public AccountDTO getUserById(String id) {
        Account account= accountRepository.findById(id).orElse(null);
        return mapToDTO(account);
    }
    @Override
    public boolean updateUser(String id,AccountDTO user) {
        Account updatedAccount= accountRepository.findById(id).orElse(null);
        if(updatedAccount==null){
            return false;
        }
        updatedAccount=mapToEntity(user);
        accountRepository.save(updatedAccount);
        return true;
    }
    @Override
    public boolean deleteUser(String id) {
        Account existingAccount = accountRepository.getById(id);
        if (existingAccount != null) {
            accountRepository.delete(existingAccount);
            return true;
        }
        return false;
    }
}
