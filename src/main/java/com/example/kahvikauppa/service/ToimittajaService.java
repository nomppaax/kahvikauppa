package com.example.kahvikauppa.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.kahvikauppa.model.Toimittaja;
import com.example.kahvikauppa.repository.ToimittajaRepository;
import com.example.kahvikauppa.repository.TuoteRepository;

@Service
public class ToimittajaService {
    @Autowired
    private ToimittajaRepository toimittajaRepository;

    @Autowired
    private TuoteRepository tuoteRepository;

    public List<Toimittaja> getAllSuppliers() {
        // Haetaan kaikki toimittajat tietokannasta
        List<Toimittaja> suppliers = this.toimittajaRepository.findAll();
        // Käydään läpi jokainen toimittaja ja lasketaan tuotteiden määrä
        for (Toimittaja supplier : suppliers) {
            // Päivitetään toimittajan tuotteiden määrä ja tallennetaan se tietokantaan
            updateProductCount(supplier);
        }
        // Palautetaan lista toimittajista
        return suppliers;
    }

    public Toimittaja getSupplierById(Long id) {
        return this.toimittajaRepository.findById(id).orElse(null);
    }

    public Toimittaja addSupplier(String name, String contactPerson, String contactPersonEmail) {
        // Tarkistetaan onko toimittaja jo tietokannassa
        Toimittaja existingSupplier = this.toimittajaRepository.findByName(name);
        if (existingSupplier != null) {
            // Toimittaja on jo olemassa, palauta se
            return existingSupplier;
        }
        // Luodaan uusi toimittaja
        Toimittaja newSupplier = new Toimittaja();
        newSupplier.setName(name.trim());
        newSupplier.setContactPerson(contactPerson.trim());
        newSupplier.setContactPersonEmail(contactPersonEmail.trim());
        // Tallennetaan uusi toimittaja tietokantaan
        return this.toimittajaRepository.save(newSupplier);
    }

    public Toimittaja updateSupplier(Long id, String name, String contactPerson, String contactPersonEmail) {
        // Etsitään toimittaja id:n perusteella
        Toimittaja supplier = getSupplierById(id);
        if (supplier != null) {
            supplier.setName(name.trim());
            supplier.setContactPerson(contactPerson.trim());
            supplier.setContactPersonEmail(contactPersonEmail.trim());
            // Tallennetaan päivitetty toimittaja tietokantaan
            return this.toimittajaRepository.save(supplier);
        }
        return null;
    }

    public void deleteSupplier(Long id) {
        this.toimittajaRepository.deleteById(id);
    }

    // Metodi päivittää toimittajan tuotteiden määrän
    private void updateProductCount(Toimittaja supplier) {
        // Haetaan toimittajan tuotteiden määrä tietokannasta
        Long productCount = this.tuoteRepository.countProductsByToimittajaID(supplier.getId());
        // Päivitetään toimittajan tuotteiden määrä
        supplier.setProductCount(productCount.intValue());
        // Tallennetaan päivitetty toimittaja tietokantaan
        this.toimittajaRepository.save(supplier);
    }

}
