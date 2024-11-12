//package com.defitech.GestUni.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/test-email")
//public class TestEmailController {
//
//    @Autowired
//    private JavaMailSender mailSender;
//
//    @GetMapping
//    public String sendTestEmail() {
//        try {
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setTo("recipient@example.com");
//            message.setSubject("Test Email");
//            message.setText("This is a test email from Spring Boot.");
//            mailSender.send(message);
//            return "Email sent successfully!";
//        } catch (Exception e) {
//            return "Error while sending email: " + e.getMessage();
//        }
//    }
//}
