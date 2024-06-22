//package aChaushev.architects.controller;
//
//import com.dictionaryapp.model.dto.UserLoginDTO;
//import com.dictionaryapp.model.dto.WordAddDTO;
//import com.dictionaryapp.service.WordService;
//import com.dictionaryapp.util.LoggedUser;
//import jakarta.validation.Valid;
//import org.springframework.stereotype.Controller;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//@Controller
//public class WordController {
//
//    private final WordService wordService;
//
//    private final LoggedUser loggedUser;
//
//    public WordController(WordService wordService, LoggedUser loggedUser) {
//        this.wordService = wordService;
//        this.loggedUser = loggedUser;
//    }
//
//    @ModelAttribute("wordAddDTO")
//    public WordAddDTO initTasksAddDTO() {
//        return new WordAddDTO();
//    }
//
//    @ModelAttribute("userLoginDTO")
//    public UserLoginDTO initLoginDTO() {
//        return new UserLoginDTO();
//    }
//
//    @GetMapping("/word/add")
//    public String getAddWord() {
//        if(loggedUser.isLogged()){
//            return "word-add";
//        }
//
//        return "redirect:/users/login";
//    }
//
//    @PostMapping("/word/add")
//    public String confirmWord(@Valid WordAddDTO wordAddDTO,
//                              BindingResult bindingResult,
//                              RedirectAttributes redirectAttributes) {
//
//        if (bindingResult.hasErrors()) {
//            redirectAttributes
//                    .addFlashAttribute("wordAddDTO", wordAddDTO)
//                    .addFlashAttribute("org.springframework.validation.BindingResult.wordAddDTO", bindingResult);
//
//            return "redirect:/word/add";
//        }
//
//        this.wordService.addWord(wordAddDTO);
//
//        return "redirect:/home";
//    }
//
//    @GetMapping("/word/remove/{id}")
//    public String removeWord(@PathVariable("id") Long WordId) {
//        if(loggedUser.isLogged()){
//            this.wordService.removeWord(WordId);
//            return "redirect:/home";
//        }
//
//        return "redirect:/";
//    }
//
//    @GetMapping("/word/remove-all")
//    public String removeAllWords() {
//        if(loggedUser.isLogged()){
//            this.wordService.removeAllWords();
//            return "redirect:/home";
//        }
//
//        return "redirect:/";
//    }
//
//
//}
