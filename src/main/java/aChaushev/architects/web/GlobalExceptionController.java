package aChaushev.architects.web;

import aChaushev.architects.service.exception.ObjectNotFoundException;
import aChaushev.architects.service.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionController {

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ObjectNotFoundException.class)
    public ModelAndView handleObjectNotFound(ObjectNotFoundException objectNotFoundException) {
        ModelAndView modelAndView = new ModelAndView("error/object-not-found");
        modelAndView.addObject("objectId", objectNotFoundException.getId());
        return modelAndView;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationExceptions(
            MethodArgumentNotValidException ex, RedirectAttributes redirectAttributes) {
        BindingResult result = ex.getBindingResult();
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.projectAddDTO", result);
        redirectAttributes.addFlashAttribute("projectAddDTO", result.getTarget());
        return "redirect:/project/add"; // Replace with your form URL
    }

    @ExceptionHandler(BindException.class)
    public String handleBindExceptions(
            BindException ex, RedirectAttributes redirectAttributes) {
        BindingResult result = ex.getBindingResult();
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.projectAddDTO", result);
        redirectAttributes.addFlashAttribute("projectAddDTO", result.getTarget());
        return "redirect:/project/add"; // Replace with your form URL
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}

