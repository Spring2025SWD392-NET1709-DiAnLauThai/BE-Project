package com.be.back_end.service.DesignService;


import com.be.back_end.dto.request.BookingCreateRequest;
import com.be.back_end.model.*;
import com.be.back_end.repository.*;
import com.be.back_end.service.CloudinaryService.ICloudinaryService;
import com.be.back_end.service.TshirtColorService.ITshirtColorService;
import com.be.back_end.service.TshirtDesignService.ITshirtDesignService;
import com.be.back_end.service.TshirtsService.ITshirtsService;
import com.be.back_end.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DesignService implements IDesignService {
    private final ICloudinaryService cloudinaryService;
    private final AccountUtils accountUtils;
    private final DesignRepository designsRepository;
    private final ITshirtsService tshirtsService;
    private final ITshirtDesignService tshirtDesignService;
    private final ITshirtColorService tshirtColorService;
    @Autowired
    public DesignService(ICloudinaryService cloudinaryService, AccountUtils accountUtils, DesignRepository designsRepository, ITshirtsService tshirtsService, ITshirtDesignService tshirtDesignService, ITshirtColorService tshirtColorService) {
        this.cloudinaryService = cloudinaryService;
        this.accountUtils = accountUtils;
        this.designsRepository = designsRepository;

        this.tshirtsService = tshirtsService;
        this.tshirtDesignService = tshirtDesignService;
        this.tshirtColorService = tshirtColorService;
    }


/*    @Override
    public List<DesignDTO> getAll() {
        List<Designs> designsList= designsRepository.findAll();
        List<DesignDTO> list= new ArrayList<>();
        for(Designs design:designsList)
        {
            list.add(mapToDTO(design));
            System.out.println(design.getId());
        }
        return list;
    }

    @Override
    public DesignDTO getById(String id) {
        Designs design= designsRepository.findById(id).orElse(null);
        return mapToDTO(design);
    }

    @Override
    public List<DesignDTO> getByName(String name) {
        return List.of();
    }

    @Override
    public DesignDTO create(DesignDTO dto) {
        Designs design= mapToEntity(dto);
        designsRepository.save(design);
        return dto;
    }

    @Override
    public boolean update(String id, DesignDTO dto) {
        Designs updatedDesign= designsRepository.findById(id).orElse(null);
        if(updatedDesign==null){
            return false;
        }
        updatedDesign=mapToEntity(dto);
        designsRepository.save(updatedDesign);
        return true;
    }

    @Override
    public boolean removeById(String id) {
        Designs existingDesign = designsRepository.getById(id);
        if (existingDesign != null) {
            designsRepository.delete(existingDesign);
            return true;
        }
        return false;
    }

    private DesignDTO mapToDTO(Designs design) {
        DesignDTO dto = new DesignDTO();


        dto.setDesignName(design.getDesignName());
        dto.setDesignFile(design.getDesignFile());
        dto.setPrice(design.getPrice());


        return dto;
    }*/

/*    private Designs mapToEntity(DesignDTO dto) {
        Designs design = new Designs();

        design.setDesignName(dto.getDesignName());
        design.setDesignFile(dto.getDesignFile());
        design.setPrice(dto.getPrice());

        return design;
    }*/



    @Override
    public Designs createAndSaveDesign(BookingCreateRequest.BookingDetailCreateRequest detailRequest) {
        Designs newDesign = new Designs();
        newDesign.setDesignFile(detailRequest.getDesignFile());
        newDesign.setAccount(accountUtils.getCurrentAccount());  // Set the current account
        return designsRepository.save(newDesign);
    }





}
