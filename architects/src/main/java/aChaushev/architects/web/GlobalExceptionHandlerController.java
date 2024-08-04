package aChaushev.architects.web;

import aChaushev.architects.service.exception.ApiObjectNotFoundException;
import aChaushev.architects.service.exception.ObjectAlreadyExistsException;
import aChaushev.architects.service.exception.ObjectNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(ObjectNotFoundException.class)
    public String handleObjectNotFoundException(ObjectNotFoundException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error";
    }

    @ExceptionHandler(ObjectAlreadyExistsException.class)
    public String handleObjectAlreadyExistsException(ObjectAlreadyExistsException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error";
    }

    @ExceptionHandler(ApiObjectNotFoundException.class)
    public String handleApiObjectAlreadyExistsException(ApiObjectNotFoundException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error";
    }

}
