package com.example.kahvikauppa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.kahvikauppa.model.Valmistaja;
import com.example.kahvikauppa.repository.TuoteRepository;
import com.example.kahvikauppa.repository.ValmistajaRepository;

@Service
public class ValmistajaService {

    @Autowired
    private TuoteRepository tuoteRepository;

    @Autowired
    private ValmistajaRepository valmistajaRepository;

    public List<Valmistaja> getAllProducers() {
        // Haetaan kaikki valmistajat tietokannasta
        List<Valmistaja> producers = this.valmistajaRepository.findAll();
        // Käydään läpi jokainen valmistaja ja lasketaan tuotteiden määrä
        for (Valmistaja producer : producers) {
            // Päivitetään valmistajan tuotteiden määrä ja tallennetaan se tietokantaan
            updateProductCount(producer);
        }
        // Palautetaan lista valmistajista
        return producers;
    }

    public Valmistaja getProducerById(Long id) {
        return this.valmistajaRepository.findById(id).orElse(null);
    }

    public Valmistaja addProducer(String name, String url) {
        // Tarkistetaan onko valmistaja jo tietokannassa
        Valmistaja existingProducer = this.valmistajaRepository.findByName(name);
        if (existingProducer != null) {
            // Valmistaja on jo olemassa, palauta se
            return existingProducer;
        }
        // Luodaan uusi valmistaja
        Valmistaja newProducer = new Valmistaja();
        newProducer.setName(name.trim());
        newProducer.setUrl(url.trim());
        // Tallennetaan uusi valmistaja tietokantaan
        return this.valmistajaRepository.save(newProducer);
    }

    public Valmistaja updateProducer(Long id, String name, String url) {
        // Etsitään valmistaja id:n perusteella
        Valmistaja producer = getProducerById(id);
        if (producer != null) {
            producer.setName(name);
            producer.setUrl(url);
            // Tallennetaan päivitetty valmistaja tietokantaan
            return this.valmistajaRepository.save(producer);
        }
        return null;
    }

    public void deleteProducer(Long id) {
        this.valmistajaRepository.deleteById(id);
    }

    // Metodi päivittää valmistajan tuotteiden määrän
    private void updateProductCount(Valmistaja producer) {
        // Haetaan valmistajan tuotteiden määrä tietokannasta
        Long productCount = this.tuoteRepository.countProductsByValmistajaID(producer.getId());
        // Päivitetään valmistajan tuotteiden määrä
        producer.setProductCount(productCount.intValue());
        // Tallennetaan päivitetty valmistaja tietokantaan
        this.valmistajaRepository.save(producer);
    }
}
