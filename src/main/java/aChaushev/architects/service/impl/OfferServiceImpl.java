package aChaushev.architects.service.impl;

import aChaushev.architects.model.dto.OfferAddDTO;
import aChaushev.architects.model.dto.OfferDTO;
import aChaushev.architects.model.entity.Offer;
import aChaushev.architects.model.entity.Project;
import aChaushev.architects.repository.OfferRepository;
import aChaushev.architects.service.OfferService;
import aChaushev.architects.service.ProjectService;
import aChaushev.architects.service.UserService;
//import org.joda.time.DateTime;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl implements OfferService {

    private ProjectService projectService;
    private OfferRepository offerRepository;
    private UserService userService;
    private ModelMapper modelMapper;

    @Autowired
    public OfferServiceImpl(ProjectService projectService, OfferRepository offerRepository,
                            UserService userService, ModelMapper modelMapper) {
        this.projectService = projectService;
        this.offerRepository = offerRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

//    @Override
//    public void create(OfferAddDTO offerAddDTO, String username) {
//        Offer offer = this.modelMapper.map(offerAddDTO, Offer.class);
//        Project project = modelMapper.map(this.projectService.findById(offerAddDTO.getProject().getId()),
//                Project.class);
//
//        if (offer.getProject().getOffer() != null) {
//            throw new OfferWithThisVehicleAlreadyExistsException("Offer with this vehicle already exists");
//        }
//        vehicle.setUser(this.modelMapper.map(this.userService.findUserByUserName(username), User.class));
//
//
//        offer.setUser(this.modelMapper.map(this.userService.findUserByUserName(username), User.class));
//        offer.setVehicle(vehicle);
//
//        LogServiceModel logServiceModel = new LogServiceModel();
//        logServiceModel.setUsername(offer.getUser().getUsername());
//        logServiceModel.setDescription(vehicle.getMaker() + ", " + vehicle.getModel() +
//                ", " + offer.getCreatedOn() + " - Add offer");
//        logServiceModel.setTime(LocalDateTime.now());
//
//        this.logService.seedLogInDB(logServiceModel);
//
//        this.offerRepository.saveAndFlush(offer);
//    }
//
//    @Override
//    public List<OfferDTO> findAll() {
//        return this.offerRepository.findAll()
//                .stream()
//                .map(c -> this.modelMapper.map(c, OfferDTO.class))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<OfferDTO> findOfferByUsername(String username) {
//        return this.offerRepository.findAllByUser_Username(username)
//                .stream()
//                .map(event -> modelMapper.map(event, OfferDTO.class))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public OfferDTO findById(String id) {
//        return this.offerRepository
//                .findById(id)
//                .map(o -> this.modelMapper.map(o, OfferDTO.class))
//                .orElseThrow(() -> new OfferNotFoundException("Offer with the given id was not found!"));
//    }
//
//    @Override
//    public void deleteOffer(String id) {
//        Offer offer = this.offerRepository
//                .findById(id)
//                .orElseThrow(() -> new OfferNotFoundException("Offer with the given id was not found!"));
//
//        LogServiceModel logServiceModel = new LogServiceModel();
//        logServiceModel.setUsername(offer.getUser().getUsername());
//        logServiceModel.setDescription(offer.getVehicle().getMaker() + ", " + offer.getCreatedOn() + " - Delete offer");
//        logServiceModel.setTime(LocalDateTime.now());
//
//        this.logService.seedLogInDB(logServiceModel);
//
//        this.offerRepository.delete(offer);
//    }
//
//    @Override
//    public OfferDTO editOffer(String id, OfferDTO offerDTO) {
//        Offer offer = this.offerRepository.findById(id)
//                .orElseThrow(() -> new OfferNotFoundException("Offer with the given id was not found!"));
//        offer.setCreatedOn(offerDTO.getCreatedOn());
//        offer.setValidUntil(offerDTO.getValidUntil());
//        offer.setPrice(offerDTO.getPrice());
//        offer.setDescription(offerDTO.getDescription());
//
//
//        LogServiceModel logServiceModel = new LogServiceModel();
//        logServiceModel.setUsername(offer.getUser().getUsername());
//        logServiceModel.setDescription(offer.getVehicle().getMaker() + ", " + offer.getCreatedOn() + " - Edit offer");
//        logServiceModel.setTime(LocalDateTime.now());
//
//        this.logService.seedLogInDB(logServiceModel);
//
//        return this.modelMapper.map(this.offerRepository.saveAndFlush(offer), OfferDTO.class);
//    }
//
//    @Scheduled(fixedRate = 5000000)
//    private void deleteOfferIfDateIsWrong() {
//        List<Offer> offers = new ArrayList<>(this.offerRepository.findAll());
//        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        DateTime nowDate = DateTime.parse(now);
//
//        for (Offer offer : offers) {
//            DateTime createdOn = DateTime.parse(offer.getCreatedOn());
//            DateTime validUntil = DateTime.parse(offer.getValidUntil());
//            if (createdOn.isAfter(validUntil) || validUntil.isBefore(nowDate)) {
//                this.offerRepository.delete(offer);
//            }
//        }
//    }
}
