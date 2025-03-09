package com.example.kahvikauppa.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
// import java.util.stream.Collectors;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.kahvikauppa.model.Tuote;
import com.example.kahvikauppa.repository.TuoteRepository;
import com.example.kahvikauppa.model.Osasto;
import com.example.kahvikauppa.repository.OsastoRepository;
import com.example.kahvikauppa.model.Toimittaja;
import com.example.kahvikauppa.repository.ToimittajaRepository;
import com.example.kahvikauppa.model.Valmistaja;
import com.example.kahvikauppa.repository.ValmistajaRepository;

@Service
public class TuoteService {

    @Autowired
    private OsastoRepository osastoRepository;

    @Autowired
    private ToimittajaRepository toimittajaRepository;

    @Autowired
    private TuoteRepository tuoteRepository;

    @Autowired
    private ValmistajaRepository valmistajaRepository;

    public List<Tuote> getAllProducts() {
        return tuoteRepository.findAll();
    }

    public Tuote getProductById(Long id) {
        return this.tuoteRepository.findById(id).orElse(null);
    }

    public List<Tuote> getProductsKahvilaitteet() {
        return tuoteRepository.findProductsByOsastoID(1L); // kaikki tuotteet osasto 1 alla
    }

    public Page<Tuote> getProductsKahvilaitteetPage(int page, int size) {
        List<Tuote> allKahvilaitteet = getProductsKahvilaitteet();
        int start = page * size;
        int end = Math.min(start + size, allKahvilaitteet.size());
        List<Tuote> pageContent = allKahvilaitteet.subList(start, end);
        return new PageImpl<>(pageContent, PageRequest.of(page, size), allKahvilaitteet.size());
    }

    public List<Tuote> getProductsKulutustuotteet() {
        // Haetaan tuotteet osaston 2 alla
        List<Tuote> osasto2Tuotteet = tuoteRepository.findProductsByOsastoID(2L);
        // Haetaan tuotteet osaston 7 alla
        List<Tuote> osasto7Tuotteet = tuoteRepository.findProductsByOsastoID(7L);
        // Yhdistetään
        osasto2Tuotteet.addAll(osasto7Tuotteet);
        // poistetaan mahdolliset duplikaatit
        Set<Tuote> yhdistetytTuotteet = new LinkedHashSet<>(osasto2Tuotteet);

        List<Tuote> kulutustuotteet = new ArrayList<>(yhdistetytTuotteet);

        return kulutustuotteet;
    }

    public Page<Tuote> getProductsKulutustuotteetPage(int page, int size) {
        List<Tuote> allKulutustuotteet = getProductsKulutustuotteet();
        int start = page * size;
        int end = Math.min(start + size, allKulutustuotteet.size());
        List<Tuote> pageContent = allKulutustuotteet.subList(start, end);
        return new PageImpl<>(pageContent, PageRequest.of(page, size), allKulutustuotteet.size());
    }

    public List<Osasto> getAllDepartments() {
        return osastoRepository.findAll();
    }

    public Osasto findDepartmentById(Long id) {
        return this.osastoRepository.findById(id).orElse(null);
    }

    public List<Toimittaja> getAllSuppliers() {
        return toimittajaRepository.findAll();
    }

    public Toimittaja findSupplierById(Long id) {
        return this.toimittajaRepository.findById(id).orElse(null);
    }

    public List<Valmistaja> getAllProducers() {
        return valmistajaRepository.findAll();
    }

    public Valmistaja findProducerById(Long id) {
        return this.valmistajaRepository.findById(id).orElse(null);
    }

    private Toimittaja createOrGetSupplier(String supplierId, String newSupplierName) {
        Toimittaja existingSupplier = null;
        if ("new".equals(supplierId)) {
            // Uusi toimittaja
            existingSupplier = toimittajaRepository.findByName(newSupplierName.trim());
            if (existingSupplier == null) {
                // Jos toimittajaa ei löydy, luodaan uusi
                existingSupplier = new Toimittaja();
                existingSupplier.setName(newSupplierName.trim());
                existingSupplier = toimittajaRepository.save(existingSupplier);
            }
        } else {
            // Hae olemassa oleva toimittaja
            existingSupplier = findSupplierById(Long.parseLong(supplierId));
        }
        return existingSupplier;
    }

    private Valmistaja createOrGetProducer(String producerId, String newProducerName) {
        Valmistaja existingProducer = null;
        if ("new".equals(producerId)) {
            // Uusi valmistaja
            existingProducer = valmistajaRepository.findByName(newProducerName.trim());
            if (existingProducer == null) {
                // Jos valmistajaa ei löydy, luodaan uusi
                existingProducer = new Valmistaja();
                existingProducer.setName(newProducerName.trim());
                existingProducer = valmistajaRepository.save(existingProducer);
            }
        } else {
            // Hae olemassa oleva valmistaja
            existingProducer = findProducerById(Long.parseLong(producerId));
        }
        return existingProducer;
    }

    public Tuote addProduct(String name, BigDecimal price, String description,
            Long departmentId, String supplierId, String newSupplierName,
            String producerId, String newProducerName, byte[] imageBytes) {

        // Haetaan osasto, toimittaja ja valmistaja niiden ID:n perusteella
        Osasto existingDepartment = findDepartmentById(departmentId);
        Toimittaja existingSupplier = createOrGetSupplier(supplierId, newSupplierName);
        Valmistaja existingProducer = createOrGetProducer(producerId, newProducerName);
        // Tarkistetaan onko tuotetta jo tietokannassa nimen perusteella
        Tuote existingProduct = this.tuoteRepository.findByName(name);
        if (existingProduct != null) {
            // Tuote on jo olemassa, palauta se
            return existingProduct;
        }
        // Uusi tuoteolio ja sille tiedot
        Tuote newProduct = new Tuote();
        newProduct.setName(name.trim());
        newProduct.setPrice(price);
        newProduct.setDescription(description);
        newProduct.setOsasto(existingDepartment);
        newProduct.setToimittaja(existingSupplier);
        newProduct.setValmistaja(existingProducer);
        newProduct.setProductImage(imageBytes);

        existingSupplier.getProducts().add(newProduct);
        existingProducer.getProducts().add(newProduct);
        existingDepartment.getProducts().add(newProduct);
        // Tallennetaan tietokantaan
        return this.tuoteRepository.save(newProduct);
    }

    public Tuote updateProduct(Long id, String name, BigDecimal price, String description,
            Long departmentId, String supplierId, String newSupplierName,
            String producerId, String newProducerName) {

        Tuote product = getProductById(id);
        if (product != null) {
            // Haetaan osasto, toimittaja ja valmistaja niiden ID:n perusteella
            Osasto existingDepartment = findDepartmentById(departmentId);
            Toimittaja existingSupplier = createOrGetSupplier(supplierId, newSupplierName);
            Valmistaja existingProducer = createOrGetProducer(producerId, newProducerName);

            product.setPrice(price);
            product.setName(name.trim());
            product.setDescription(description.trim());
            product.setOsasto(existingDepartment);
            product.setToimittaja(existingSupplier);
            product.setValmistaja(existingProducer);

            existingSupplier.getProducts().add(product);
            existingProducer.getProducts().add(product);
            existingDepartment.getProducts().add(product);
            // Tallennetaan tietokantaan
            return this.tuoteRepository.save(product);
        }
        return null;
        // jätetään tuotteiden päivittämisestä kuvan päivitys-optio pois, en saa sitä
        // millään filen kenttään
        // byte[] imageBytes
        // product.setProductImage(imageBytes);
    }

    public void deleteProduct(Long id) {
        this.tuoteRepository.deleteById(id);
    }

    public List<Tuote> searchMachines(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return getProductsKahvilaitteet();
        } else {
            return tuoteRepository.findByNameLikeIgnoreCaseAndOsastoId("%" + keyword + "%", 1L);
        }
    }

    public List<Tuote> searchConsumerProducts(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return getProductsKulutustuotteet();
        } else {
            // Hae tuotteet osaston 2 ja 7 alla ja lisää ne yhteen
            List<Tuote> osasto2Tuotteet = tuoteRepository.findByNameLikeIgnoreCaseAndOsastoId("%" + keyword + "%", 2L);
            List<Tuote> osasto7Tuotteet = tuoteRepository.findByNameLikeIgnoreCaseAndOsastoId("%" + keyword + "%", 7L);
            osasto2Tuotteet.addAll(osasto7Tuotteet);
            Set<Tuote> yhdistetytTuotteet = new LinkedHashSet<>(osasto2Tuotteet);
            // Muunnetaan listaksi
            return new ArrayList<>(yhdistetytTuotteet);
        }
    }

}
