package com.bfs.quizlet.controller;

import com.bfs.quizlet.service.ContactService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/contact")
public class ContactController {
    private final ContactService contactService;
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("")
    public String getContactUsPage() {
        return "contact";
    }

    @PostMapping("/send")
    public String submitContactUsForm(@RequestParam(name = "subject")String subject,
                                      @RequestParam(name = "message")String message,
                                      @RequestParam(name = "email")String email, Model model) {
        try{
            contactService.addContact(subject, message, email);
            model.addAttribute("successMessage", "Your message has been sent successfully!");
        }catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/contact";
        }
        return "redirect:/contact";
    }
}
