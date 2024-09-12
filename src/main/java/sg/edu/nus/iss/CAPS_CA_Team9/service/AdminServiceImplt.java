package sg.edu.nus.iss.CAPS_CA_Team9.service;

import java.util.List;
//import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.CAPS_CA_Team9.model.Admin;
import sg.edu.nus.iss.CAPS_CA_Team9.repository.AdminRepository;

@Service
public class AdminServiceImplt implements AdminService {
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    @Override
    public Admin createAdmin(Admin admin) {
        if(admin.getAdminPwd() != null) {
            String hashedPassword = bCryptPasswordEncoder.encode(admin.getAdminPwd());
            admin.setAdminPwd(hashedPassword);
        }
        return adminRepository.saveAndFlush(admin);
    }

    @Transactional
    @Override
    public Admin updateAdmin(Admin admin) {
        Admin oldAdmin = adminRepository.findById(admin.getId()).orElse(null);
        oldAdmin.setAdminName(admin.getAdminName());
        if(admin.getAdminPwd()!=null) {
            String hashedPassword = bCryptPasswordEncoder.encode(admin.getAdminPwd());
            oldAdmin.setAdminPwd(hashedPassword);
        }
        return adminRepository.saveAndFlush(oldAdmin);
    }

    @Transactional
    @Override
    public void deleteAdmin(Admin admin) {
        adminRepository.delete(admin);
    }

    @Transactional
    @Override
    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    @Override
    public Admin getLastAdmin() {

        return adminRepository.findTopByOrderByIdDesc();

    }

    @Override
    public Admin getAdminById(String id) {
        Admin found = adminRepository.findById(id).orElse(null);
        return found;
    }
}
