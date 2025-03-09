package com.example.kahvikauppa.controller;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.kahvikauppa.model.Tuote;
import com.example.kahvikauppa.service.ValmistajaService;
import com.example.kahvikauppa.service.TuoteService;
import com.example.kahvikauppa.service.OsastoService;
import com.example.kahvikauppa.service.TilausService;
import com.example.kahvikauppa.model.Osasto;
import com.example.kahvikauppa.model.Tilaus;
import com.example.kahvikauppa.model.Toimittaja;
import com.example.kahvikauppa.model.Valmistaja;
import com.example.kahvikauppa.service.ToimittajaService;

@Controller
public class KahvikauppaController {

    @Autowired
    private TilausService tilausService;

    @Autowired
    private TuoteService tuoteService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/kahvilaitteet")
    public String machines(@ModelAttribute("message") String message, Model model,
            @RequestParam(defaultValue = "0") int page) {
        if (!message.isEmpty()) {
            model.addAttribute("message", message);
        } else {
            model.addAttribute("message", false);
        }
        int pageSize = 9; // Haluttu rivien määrä yhdellä sivulla
        Page<Tuote> tuotePage = tuoteService.getProductsKahvilaitteetPage(page, pageSize);
        List<Tuote> kahvilaitteet = tuotePage.getContent();
        model.addAttribute("kahvilaitteet", kahvilaitteet);
        model.addAttribute("totalPages", tuotePage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageNumber", page + 1); // Lisää sivunumeron malliin
        // kaikki tuotteet osasto 1 alla
        List<Tuote> kaikkiKahvilaitteet = tuoteService.getProductsKahvilaitteet();
        model.addAttribute("kaikkiKahvilaitteet", kaikkiKahvilaitteet);
        return "kahvilaitteet";
    }

    @GetMapping("/laite/{id}")
    public String individualMachinePage(@PathVariable Long id, Model model) {
        Tuote product = tuoteService.getProductById(id);

        if (product == null) {
            return "redirect:/kahvilaitteet";
        }
        String imageURL = "/productImage/" + id;
        model.addAttribute("imageURL", imageURL);
        model.addAttribute("product", product);

        return "laite";
    }

    @GetMapping("/kulutustuotteet")
    public String consumerProducts(@ModelAttribute("message") String message, Model model,
            @RequestParam(defaultValue = "0") int page) {
        if (!message.isEmpty()) {
            model.addAttribute("message", message);
        } else {
            model.addAttribute("message", false);
        }
        int pageSize = 9; // Haluttu tuotteiden määrä yhdellä sivulla
        Page<Tuote> tuotePage = tuoteService.getProductsKulutustuotteetPage(page, pageSize);
        List<Tuote> kulutustuotteet = tuotePage.getContent();
        model.addAttribute("kulutustuotteet", kulutustuotteet);
        model.addAttribute("totalPages", tuotePage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageNumber", page + 1);
        List<Tuote> kaikkiKulutustuotteet = tuoteService.getProductsKulutustuotteet();
        model.addAttribute("kaikkiKulutustuotteet", kaikkiKulutustuotteet);
        return "kulutustuotteet";
    }

    @GetMapping("/tuote/{id}")
    public String individualProductPage(@PathVariable Long id, Model model) {
        Tuote product = tuoteService.getProductById(id);

        if (product == null) {
            return "redirect:/kulutustuotteet";
        }
        String imageURL = "/productImage/" + id;
        model.addAttribute("imageURL", imageURL);
        model.addAttribute("product", product);

        return "tuote";
    }

    // KUVIEN PURKAMINEN
    @GetMapping("/productImage/{id}")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long id) {
        Tuote product = tuoteService.getProductById(id);
        if (product != null && product.getProductImage() != null) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(product.getProductImage(), headers,
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // SEARCH TOIMINNALLISUUDET
    @GetMapping("/searchMachines")
    public String searchMachines(@RequestParam String keyword, Model model) {
        List<Tuote> machines = tuoteService.searchMachines(keyword);
        List<Tuote> kaikkiKahvilaitteet = tuoteService.getProductsKahvilaitteet();
        if (machines.isEmpty()) {
            model.addAttribute("keyword", "Hakuehtoa vastaavia tuotteita ei löytynyt kahvilaitteistamme");
            model.addAttribute("keywordExists", true);
        }
        model.addAttribute("kahvilaitteet", machines);
        model.addAttribute("keywordExists", true);
        model.addAttribute("kaikkiKahvilaitteet", kaikkiKahvilaitteet);
        return "kahvilaitteet";
    }

    @GetMapping("/searchConsumerProducts")
    public String searchProducts(@RequestParam String keyword, Model model) {
        List<Tuote> products = tuoteService.searchConsumerProducts(keyword);
        List<Tuote> kaikkiKulutustuotteet = tuoteService.getProductsKulutustuotteet();
        if (products.isEmpty()) {
            model.addAttribute("keyword", "Hakuehtoa vastaavia tuotteita ei löytynyt kulutustuotteistamme");
            model.addAttribute("keywordExists", true);
        }
        model.addAttribute("kulutustuotteet", products);
        model.addAttribute("keywordExists", true);
        model.addAttribute("kaikkiKulutustuotteet", kaikkiKulutustuotteet);
        return "kulutustuotteet";
    }

    // TILAUSLISTAN VASTAANOTTAMISEN
    @PostMapping("/sendOrder")
    public String newOrder(@RequestParam String order, @RequestParam String page,
            RedirectAttributes redirectAttributes) {
        String message;
        try {
            this.tilausService.newOrder(order);
            message = "Kiitos! Olemme vastaanottaneet tilauksesi ja käsittelemme sitä parhaillaan. Lähetämme sinulle tilausvahvistuksen sähköpostitse pian.<br>"
                    +
                    "Mikäli sinulla on kysyttävää tilauksestasi tai tarvitset lisätietoja, älä epäröi ottaa yhteyttä asiakaspalveluumme.<br>"
                    +
                    "Ystävällisin terveisin, Töölön Kahvikaupan tiimi";
        } catch (RuntimeException e) {
            message = "Tilausta lähettäessä sattui ongelma. Pahoittelemme mahdollisia aiheutuneita häiriöitä. Otathan yhteyttä asiakaspalveluumme saadaksesi lisätietoja ja apua."
                    + e.getMessage();
        }
        redirectAttributes.addFlashAttribute("message", message);
        // Tarkista, mikä sivu lähetti tilauksen ja palaa sille sivulle
        if ("kulutustuotteet".equals(page)) {
            return "redirect:/kulutustuotteet";
        }
        return "redirect:/kahvilaitteet";
    }
}
