package com.bfs.quizlet.controller;

import com.bfs.quizlet.domain.Category;
import com.bfs.quizlet.domain.Contact;
import com.bfs.quizlet.domain.Quiz;
import com.bfs.quizlet.domain.User;
import com.bfs.quizlet.dto.AdminStatsDTO;
import com.bfs.quizlet.dto.Page;
import com.bfs.quizlet.dto.QuestionDTO;
import com.bfs.quizlet.dto.UserManageDTO;
import com.bfs.quizlet.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final QuestionService questionService;
    private final QuizService quizService;
    private final ContactService contactService;

    public AdminController(AdminService adminService, UserService userService,
                           CategoryService categoryService, QuestionService questionService, QuizService quizService, ContactService contactService) {
        this.adminService = adminService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.questionService = questionService;
        this.quizService = quizService;
        this.contactService = contactService;
    }
    private static final String DEFAULT_CATEGORY_NAME = "New Category";

    @GetMapping("")
    public String getStatistic(Model model) {
        AdminStatsDTO adminStats = adminService.getAdminStats();
        model.addAttribute("adminStats", adminStats);
        return "admin";
    }

                                            /** User management */
    @GetMapping("/users")
    public String getUsers(@RequestParam(name = "page", defaultValue = "1") int currentPage,
                           @RequestParam(name = "size", defaultValue = "5") int pageSize,
                           Model model) {
        Page<UserManageDTO> pageOfUsers = adminService.getAllUsersInPage(currentPage, pageSize);
        model.addAttribute("users",pageOfUsers.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", pageOfUsers.getTotalPages());
        return "admin-users";
    }

    @PostMapping("/users/toggle-status")
    public String toggleUserStatus(@RequestParam("userId") Long userId,
                                   HttpSession session){
        User user = (User) session.getAttribute("user");
        userService.toggleUserStatus(userId, user.getUserId());
        return "redirect:/admin/users";
    }

                                            /** Quiz Result management */
    @GetMapping("/quiz-results")
    public String getQuizzes(@RequestParam(name = "filterCategoryId", defaultValue = "0") Long categoryId,
                             @RequestParam(name = "filterCreatorId", defaultValue = "0") Long createdBy,
                             @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
                             @RequestParam(name = "page", defaultValue = "1") int currentPage,
                             @RequestParam(name = "size", defaultValue = "5") int pageSize,
                             Model model) {
        List<Category> categories = categoryService.getAllCategories();
        List<UserManageDTO> users = adminService.getAllUsers();
        model.addAttribute("categories", categories);
        model.addAttribute("users", users);
        Page<Quiz> pageOfQuizResults = quizService.pageFilterAndSort(currentPage,pageSize,categoryId,createdBy,sortBy);
        model.addAttribute("quizResults", pageOfQuizResults.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", pageOfQuizResults.getTotalPages());
        model.addAttribute("filterCategoryId", categoryId);
        model.addAttribute("filterCreatorId", createdBy);
        model.addAttribute("sortBy",sortBy);
        return "admin-quizzes";
    }

                                            /** Question management */
    @GetMapping("/questions")
    public String getCategoriesAndQuestions(
            @RequestParam(name = "selectedCategoryId", defaultValue = "1") Long selectedCategoryId,
            @RequestParam(name = "page", defaultValue = "1") int currentPage,
            @RequestParam(name = "size", defaultValue = "2") int pageSize,
            Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        Optional<Category> selectedCategory = categoryService.getCategoryById(selectedCategoryId);
        if (selectedCategory.isPresent()){
            model.addAttribute("selectedCategoryId", selectedCategoryId);
            model.addAttribute("selectedCategoryName", selectedCategory.get().getName());
            model.addAttribute("selectedCategory", selectedCategory.get());
        }else{
            model.addAttribute("selectedCategoryId", 0);
            model.addAttribute("selectedCategoryName", "No such Category");
        }
        Page<QuestionDTO> pageOfQuestions = questionService.getPageOfQuestionsByCategoryId(selectedCategoryId, currentPage, pageSize);
        model.addAttribute("questions", pageOfQuestions.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", pageOfQuestions.getTotalPages());
        return "admin-questions";
    }

    @PostMapping("/categories/create")
    public String createCategory(
            @RequestParam(name = "categoryName", defaultValue = DEFAULT_CATEGORY_NAME) String categoryName,
            @RequestParam(name = "page", defaultValue = "1") int currentPage,
            @RequestParam(name = "size", defaultValue = "5") int pageSize,
            HttpSession session) {
        User user = (User) session.getAttribute("user");
        Long newCategoryId = categoryService.createCategory(categoryName, user.getUserId());
        return "redirect:/admin/questions?selectedCategoryId=" + newCategoryId + "&page=" + currentPage + "&size=" + pageSize;
    }

    @PostMapping("/categories/toggle-status")
    public String toggleCategoryStatus(
            @RequestParam(name = "categoryId") Long categoryId,
            @RequestParam(name = "page", defaultValue = "1") int currentPage,
            @RequestParam(name = "size", defaultValue = "5") int pageSize,
            HttpSession session) {
        User user = (User) session.getAttribute("user");
        categoryService.toggleCategoryStatus(categoryId, user.getUserId());
        return "redirect:/admin/questions?selectedCategoryId=" + categoryId + "&page=" + currentPage + "&size=" + pageSize;
    }

    @PostMapping("/categories/rename")
    public String renameCategory(
            @RequestParam(name = "categoryId") Long categoryId,
            @RequestParam(name = "newCategoryName") String newCategoryName,
            @RequestParam(name = "page", defaultValue = "1") int currentPage,
            @RequestParam(name = "size", defaultValue = "5") int pageSize,
            HttpSession session) {
        User user = (User) session.getAttribute("user");
        categoryService.renameCategory(categoryId, newCategoryName, user.getUserId());
        return "redirect:/admin/questions?selectedCategoryId=" + categoryId + "&page=" + currentPage + "&size=" + pageSize;
    }

    @GetMapping("questions/create/{categoryId}")
    public String createQuestion(
            @PathVariable(name = "categoryId") Long categoryId,
            HttpSession session) {
        User user = (User) session.getAttribute("user");
        Long questionId = questionService.createQuestion(categoryId, user.getUserId());
        return "redirect:/admin/question-edit/"+questionId;
    }

    @PostMapping("/questions/toggle-status")
    public String toggleQuestionStatus(
            @RequestParam(name = "questionId") Long questionId,
            @RequestParam(name = "categoryId") Long categoryId,
            @RequestParam(name = "page", defaultValue = "1") int currentPage,
            @RequestParam(name = "size", defaultValue = "5") int pageSize,
            HttpSession session) {
        User user = (User) session.getAttribute("user");
        questionService.toggleQuestionStatus(questionId, user.getUserId());
        return "redirect:/admin/questions?selectedCategoryId=" + categoryId + "&page=" + currentPage + "&size=" + pageSize;
    }

    @GetMapping("/question-edit/{questionId}")
    public String editQuestion(
            @PathVariable(name = "questionId") Long questionId,
            Model model) {
        Optional<QuestionDTO> question = questionService.getQuestionById(questionId);
        question.ifPresent(questionDTO -> model.addAttribute("question", questionDTO));
        return "admin-question-edit";
    }

    @PostMapping("/question-edit/{questionId}/update")
    public String updateQuestion(@PathVariable("questionId") Long questionId,
                                 @ModelAttribute QuestionDTO questionDTO,
                                 HttpSession session) {
        User user = (User) session.getAttribute("user");
        questionService.updateQuestion(questionDTO, user.getUserId());
        return "redirect:/admin/question-edit/"+questionId;
    }

    @GetMapping("/question-edit/{questionId}/add-choice")
    public String addChoiceForQuestion(
            @PathVariable(name = "questionId") Long questionId,
            HttpSession session) {
        User user = (User) session.getAttribute("user");
        questionService.addChoiceForQuestion(questionId, user.getUserId());
        return "redirect:/admin/question-edit/"+questionId;
    }

                                    /** Messages management */
    @GetMapping("/messages")
    public String getMessages(Model model) {
        List<Contact> contacts = contactService.getAllContacts();
        model.addAttribute("contacts", contacts);
        return "admin-contact";
    }

    @GetMapping("/messages/{contactId}")
    public String getMessage(@PathVariable(name = "contactId") Long contactId, Model model) {
        Optional<Contact> contact = contactService.getContactById(contactId);
        contact.ifPresent(value -> model.addAttribute("contact", contact.get()));
        return "admin-contact-detail";
    }

    @PostMapping("/messages/update")
    public String updateMessage(@RequestParam(name = "contactId") Long contactId,
                                @RequestParam(name = "isActive", defaultValue = "true") Boolean isActive,
                                @RequestParam(name = "isSolved") Boolean isSolved,
                                HttpSession session) {
        User user = (User) session.getAttribute("user");
        contactService.updateContact(contactId, isActive, isSolved, user.getUserId());
        return "redirect:/admin/messages";
    }
}
