package com.example.kahvikauppa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.kahvikauppa.model.Osasto;
import com.example.kahvikauppa.repository.OsastoRepository;
import com.example.kahvikauppa.repository.TuoteRepository;

@Service
public class OsastoService {

    @Autowired
    private OsastoRepository osastoRepository;

    @Autowired
    private TuoteRepository tuoteRepository;

    public List<Osasto> getAllDepartments() {
        // Haetaan kaikki osastot tietokannasta
        List<Osasto> departments = this.osastoRepository.findAll();
        // Käydään läpi jokainen osasto ja lasketaan tuotteiden määrä
        for (Osasto department : departments) {
            // Päivitetään osaston tuotteiden määrä ja tallennetaan se tietokantaan
            updateProductCount(department);
        }
        // Palautetaan lista valmistajista
        return departments;
    }

    public Osasto getDepartmentById(Long id) {
        return this.osastoRepository.findById(id).orElse(null);
    }

    public List<Osasto> getDepartmentByIDP(Long osastoIDP) {
        List<Osasto> subDepartments = this.osastoRepository.findByOsastoIDP(osastoIDP);
        if (osastoIDP.equals(2L)) {
            List<Osasto> subDepartment7 = this.osastoRepository.findByOsastoIDP(7L);
            subDepartments.addAll(subDepartment7);
        }
        return subDepartments;
    }

    public Osasto addDepartment(String name, Long osastoIDP) {
        // Tarkistetaan onko osastoa jo tietokannassa
        Osasto existingDepartment = this.osastoRepository.findByName(name);
        if (existingDepartment != null) {
            // Osasto on jo olemassa, palauta se
            return existingDepartment;
        }
        // Luodaan uusi osasto
        Osasto newDepartment = new Osasto();
        newDepartment.setName(name.trim());
        newDepartment.setOsastoIDP(osastoIDP);
        // Tallennetaan uusi osasto tietokantaan
        return this.osastoRepository.save(newDepartment);
    }

    public Osasto updateDepartment(Long id, String name, Long osastoIDP) {
        // Etsitään osasto id:n perusteella
        Osasto department = getDepartmentById(id);
        if (department != null) {
            department.setName(name.trim());
            department.setOsastoIDP(osastoIDP);
            // Tallennetaan päivitetty osasto tietokantaan
            return this.osastoRepository.save(department);
        }
        return null;
    }

    public void deleteDepartment(Long id) {
        this.osastoRepository.deleteById(id);
    }

    private void updateProductCount(Osasto department) {
        Long productCount = this.tuoteRepository.countProductsByOsastoID(department.getId());

        // Tarkistetaan, onko osasto yksi niistä, joihin tarvitaan alaosastojen
        // tuotteiden yhdistämistä
        if (department.getId().equals(1L) || department.getId().equals(7L) || department.getId().equals(2L)) {
            // Haetaan alaosastot
            List<Osasto> subDepartments = getDepartmentByIDP(department.getId());

            // Käydään läpi jokainen alaosasto
            for (Osasto subDepartment : subDepartments) {
                // Lisätään alaosaston tuotteiden määrä yläosaston tuotteiden määrään
                productCount += this.tuoteRepository.countProductsByOsastoID(subDepartment.getId());
            }
        }
        // Päivitetään osaston tuotteiden määrä
        department.setProductCount(productCount.intValue());
        // Tallennetaan päivitetty osasto tietokantaan
        this.osastoRepository.save(department);
    }

}
