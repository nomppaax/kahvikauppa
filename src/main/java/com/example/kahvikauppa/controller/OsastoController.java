package com.example.kahvikauppa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.kahvikauppa.model.Osasto;
import com.example.kahvikauppa.service.OsastoService;

@Controller
public class OsastoController {

    @Autowired
    private OsastoService osastoService;

    @GetMapping("/osastot")
    public String departments(@ModelAttribute("message") String message, Model model) {
        List<Osasto> departments = this.osastoService.getAllDepartments();
        if (!message.isEmpty()) {
            model.addAttribute("message", message);
        } else {
            model.addAttribute("message", false);
        }
        model.addAttribute("departments", departments);
        return "osastot";
    }

    @PostMapping("/osastot")
    public String addDepartment(@RequestParam String name, @RequestParam Long osastoIDP) {
        this.osastoService.addDepartment(name, osastoIDP);
        return "redirect:/osastot";
    }

    @GetMapping("/updateDepartment/{id}")
    public String getUpdateDepartmentPage(@PathVariable Long id, Model model) {
        Osasto department = this.osastoService.getDepartmentById(id);
        if (department != null) {
            model.addAttribute("department", department);
            return "muokkaa-osastoa";
        }
        return "redirect:/osastot";
    }

    @PostMapping("/updateDepartment/{id}")
    public String updateDepartment(@PathVariable Long id, @RequestParam String name, @RequestParam Long osastoIDP) {
        this.osastoService.updateDepartment(id, name, osastoIDP);
        return "redirect:/osastot";
    }

    @PostMapping("/deleteDepartment/{id}")
    public String deleteDepartment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        String message;
        try {
            this.osastoService.deleteDepartment(id);
            message = "Osasto poistettu onnistuneesti tietokannasta.";
        } catch (RuntimeException e) {
            message = "Osastoa ei voi poistaa, koska siihen liittyy tuotteita tietokannassa (ks. tuotteiden määrä valikoimassa).";
        }
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/osastot";
    }
}
