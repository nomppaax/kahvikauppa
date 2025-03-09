package com.example.kahvikauppa.controller;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.kahvikauppa.model.Tuote;
import com.example.kahvikauppa.service.TuoteService;

@Controller
public class TuoteController {

    @Autowired
    private TuoteService tuoteService;

    @GetMapping("/admin")
    public String admin(@ModelAttribute("message") String message, Model model) {
        model.addAttribute("products", this.tuoteService.getAllProducts());
        model.addAttribute("departments", this.tuoteService.getAllDepartments());
        model.addAttribute("suppliers", this.tuoteService.getAllSuppliers());
        model.addAttribute("producers", this.tuoteService.getAllProducers());
        model.addAttribute("isNewSupplier", true);
        model.addAttribute("isNewProducer", true);
        if (!message.isEmpty()) {
            model.addAttribute("message", message);
        } else {
            model.addAttribute("message", false);
        }

        return "admin";
    }

    @PostMapping("/admin")
    public String addProduct(@RequestParam String name, @RequestParam BigDecimal price,
            @RequestParam String description, @RequestParam Long departmentId, @RequestParam String supplierId,
            @RequestParam String newSupplierName, @RequestParam String producerId,
            @RequestParam String newProducerName, @RequestParam("productImage") MultipartFile productImage,
            Model model) throws IOException {

        byte[] imageBytes = productImage.getBytes();

        // TuoteService uuden tuotteen lisäämiseen
        this.tuoteService.addProduct(name, price, description, departmentId,
                supplierId, newSupplierName, producerId, newProducerName,
                imageBytes);

        return "redirect:/admin";
    }

    @GetMapping("/updateProduct/{id}")
    public String getUpdateProductPage(@PathVariable Long id, Model model) {
        // Haetaan tuote tietokannasta tuotteen id:n perusteella
        Tuote product = this.tuoteService.getProductById(id);
        if (product == null) {
            // Jos tuotetta ei löydy hjataan takaisin admin-sivulle
            return "redirect:/admin";
        }
        String imageURL = "/productImage/" + id;

        model.addAttribute("product", product);
        model.addAttribute("description", product.getDescription()); // Lisätään tuotteen kuvaus attribuuttina
        model.addAttribute("imageURL", imageURL); // Lisätään kuvan URL attribuuttina
        model.addAttribute("departments", this.tuoteService.getAllDepartments());
        model.addAttribute("suppliers", this.tuoteService.getAllSuppliers());
        model.addAttribute("producers", this.tuoteService.getAllProducers());
        model.addAttribute("isNewSupplier", true); // Ei luoda uutta toimittajaa muokkauksessa
        model.addAttribute("isNewProducer", true); // Ei luoda uutta valmistajaa muokkauksessa

        return "muokkaa-tuotetta"; // Palautetaan näkymän nimi
    }

    @PostMapping("/updateProduct/{id}")
    public String updateProduct(@PathVariable Long id, @RequestParam String name, @RequestParam BigDecimal price,
            @RequestParam String description, @RequestParam Long departmentId, @RequestParam String supplierId,
            @RequestParam String newSupplierName, @RequestParam String producerId,
            @RequestParam String newProducerName,
            Model model) {

        // TuoteService uuden tuotteen lisäämiseen
        this.tuoteService.updateProduct(id, name, price, description, departmentId,
                supplierId, newSupplierName, producerId, newProducerName);

        return "redirect:/admin";
        // sama muutos tänne tuotekuvanpäivittämisen osalta
        // @RequestParam("productImage") MultipartFile productImage,
        // throws IOException
        // byte[] imageBytes = productImage.getBytes();
        // imageBytes
    }

    @PostMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        String message;
        try {
            this.tuoteService.deleteProduct(id);
            message = "Tuote poistettu onnistuneesti tietokannasta.";
        } catch (RuntimeException e) {
            message = "Jotain meni pieleen ja tuotetta ei voitu poistaa tietokannasta.";
        }
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/admin";
    }
}
