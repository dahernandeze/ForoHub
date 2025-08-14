package alura.forohub.api.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "¡Hola! ForoHub API está funcionando correctamente 🚀";
    }

    @GetMapping("/status")
    public String status() {
        return "API Status: OK ✅";
    }
}