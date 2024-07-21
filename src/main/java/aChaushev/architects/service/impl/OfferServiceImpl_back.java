//package aChaushev.architects.service.impl;
//
//import aChaushev.architects.model.dto.OfferAddDTO;
//import aChaushev.architects.model.entity.Offer;
//import aChaushev.architects.model.entity.Project;
//import aChaushev.architects.model.entity.User;
//import aChaushev.architects.repository.OfferRepository;
//import aChaushev.architects.repository.UserRepository;
//import aChaushev.architects.service.OfferService;
//import aChaushev.architects.service.UserService;
//import aChaushev.architects.service.exception.ObjectNotFoundException;
//import aChaushev.architects.service.exception.OfferWithThisVehicleAlreadyExistsException;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class OfferServiceImpl_back implements OfferService {
//
//    private final OfferService offerService;
//    private final OfferRepository offerRepository;
//    private final UserRepository userRepository;
//    private final UserService userService;
//    private final ModelMapper modelMapper;
//
//    @Autowired
//    public OfferServiceImpl_back(OfferService offerService, OfferRepository offerRepository, UserRepository userRepository, UserService userService,
//                                 ModelMapper modelMapper) {
//        this.offerService = offerService;
//        this.offerRepository = offerRepository;
//        this.userRepository = userRepository;
//        this.userService = userService;
//        this.modelMapper = modelMapper;
//    }
//
//    @Override
//    public void create(OfferAddDTO offerAddDTO, Long userId) {
//        User user = this.userRepository.findById(userId)
//                .orElseThrow(() -> new ObjectNotFoundException("User not found", userId));
//        Offer offer = this.modelMapper.map(offerAddDTO, Offer.class);
//        Project project = modelMapper.map(this.offerService.findById(offerAddDTO.getProject().getId()),
//                Project.class);
//
//        if (offer.getProject().getOffer() != null) {
//            throw new OfferWithThisVehicleAlreadyExistsException("Offer with this project already exists");
//        }
//
//        project.setArchitect(this.modelMapper.map(user, User.class));
//        offer.setUser(this.modelMapper.map(user, User.class));
//        offer.setProject(project);
//
//        LogServiceModel logServiceModel = new LogServiceModel();
//        logServiceModel.setUsername(offer.getUser().getUsername());
//        logServiceModel.setDescription(project.getMaker() + ", " + project.getModel() +
//                ", " + offer.getCreatedOn() + " - Add offer");
//        logServiceModel.setTime(LocalDateTime.now());
//
//        this.logService.seedLogInDB(logServiceModel);
//
//        this.offerRepository.saveAndFlush(offer);
//    }
//
//    @Override
//    public List<OfferAddDTO> findAll() {
//        return this.offerRepository.findAll()
//                .stream()
//                .map(c -> this.modelMapper.map(c, OfferAddDTO.class))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<OfferAddDTO> findOfferByUsername(String username) {
//        return this.offerRepository.findAllByUser_Username(username)
//                .stream()
//                .map(event -> modelMapper.map(event, OfferAddDTO.class))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public OfferAddDTO findById(Long id) {
//        return this.offerRepository
//                .findById(id)
//                .map(o -> this.modelMapper.map(o, OfferAddDTO.class))
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
//        logServiceModel.setDescription(offer.getProject().getMaker() + ", " + offer.getCreatedOn() + " - Delete offer");
//        logServiceModel.setTime(LocalDateTime.now());
//
//        this.logService.seedLogInDB(logServiceModel);
//
//        this.offerRepository.delete(offer);
//    }
//
//    @Override
//    public OfferAddDTO editOffer(String id, OfferAddDTO offerAddDTO) {
//        Offer offer = this.offerRepository.findById(id)
//                .orElseThrow(() -> new OfferNotFoundException("Offer with the given id was not found!"));
//        offer.setCreatedOn(offerAddDTO.getCreatedOn());
//        offer.setValidUntil(offerAddDTO.getValidUntil());
//        offer.setPrice(offerAddDTO.getPrice());
//        offer.setDescription(offerAddDTO.getDescription());
//
//
//        LogServiceModel logServiceModel = new LogServiceModel();
//        logServiceModel.setUsername(offer.getUser().getUsername());
//        logServiceModel.setDescription(offer.getProject().getMaker() + ", " + offer.getCreatedOn() + " - Edit offer");
//        logServiceModel.setTime(LocalDateTime.now());
//
//        this.logService.seedLogInDB(logServiceModel);
//
//        return this.modelMapper.map(this.offerRepository.saveAndFlush(offer), OfferAddDTO.class);
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
//}
