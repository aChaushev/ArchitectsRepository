//package aChaushev.architects.controller;
//
//import com.dictionaryapp.model.dto.WordDTO;
//import com.dictionaryapp.service.WordService;
//import com.dictionaryapp.util.LoggedUser;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Controller
//public class HomeController {
//
//    private final WordService wordService;
//
//    private final LoggedUser loggedUser;
//
//    public HomeController(WordService wordService, LoggedUser loggedUser) {
//        this.wordService = wordService;
//        this.loggedUser = loggedUser;
//    }
//
//    @GetMapping("/")
//    public String getIndexPage() {
//        if (loggedUser.isLogged()) {
//            return "redirect:/home";
//        }
//        return "index";
//
//    }
//
//    @GetMapping("/home")
//    public String getHomePage(Model model) {
//        if (!loggedUser.isLogged()) {
//            return "redirect:/";
//        }
//
//        List<WordDTO> allWordDTOs = wordService.getAllWords();
//        model.addAttribute("wordsList", allWordDTOs);
//        model.addAttribute("allWordsCount", allWordDTOs.size());
//        model.addAttribute("loggedUserName", loggedUser.getUsername());
//
//        List<WordDTO> wordsGermanList = new ArrayList<>();
//        List<WordDTO> wordsSpanishList = new ArrayList<>();
//        List<WordDTO> wordsFrenchList = new ArrayList<>();
//        List<WordDTO> wordsItalianList = new ArrayList<>();
//
//        for (WordDTO wordDTO : allWordDTOs) {
//            switch (wordDTO.getLanguageName().name()) {
//                case "GERMAN" -> wordsGermanList.add(wordDTO);
//                case "SPANISH" -> wordsSpanishList.add(wordDTO);
//                case "FRENCH" -> wordsFrenchList.add(wordDTO);
//                case "ITALIAN" -> wordsItalianList.add(wordDTO);
//            }
//        }
//
//        model.addAttribute("germanWords", wordsGermanList);
//        model.addAttribute("spanishWords", wordsSpanishList);
//        model.addAttribute("frenchWords", wordsFrenchList);
//        model.addAttribute("italianWords", wordsItalianList);
//
//        return "home";
//
//    }
//
//}
