package ao.dumij.day2day.controllers;

import ao.dumij.day2day.models.AtividadeDiaria;
import ao.dumij.day2day.repository.AtividadeDiariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class AtividadeDiariaController {

    private final AtividadeDiariaRepository repository;
    private String dia;
    private String redirect;

    @Autowired
    public AtividadeDiariaController(AtividadeDiariaRepository repository) {
        this.repository = repository;
        dia = "";
        redirect = "";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/hoje";
    }

    @GetMapping("/{dia}")
    public String lista(@PathVariable String dia) {

        this.dia = !"".equals(dia) ? dia : "hoje";

        return "redirect:/atividades";
    }

    @GetMapping("/atividades")
    public ModelAndView listaAtividades() {

        List<AtividadeDiaria> diarias = null;

        redirect = "atividades";

        switch (this.dia) {
            case "anterior":
                diarias = repository.findByDataBefore(new Date());
                break;
            case "hoje":
                diarias = repository.findByData(new Date());
                break;
            case "posterior":
                diarias = repository.findByDataAfter(new Date());
                break;
            case "tudo":
                diarias = (List<AtividadeDiaria>) repository.findAll();
                break;
            default:
                diarias = new ArrayList<>();
                break;
        }

        return new ModelAndView("atividade/lista").addObject("ativs", diarias).addObject("listaDe", this.dia)
                .addObject("atividadeDiaria", new AtividadeDiaria());
    }

    @PostMapping("/atividades")
    public ModelAndView listaAtividadesPost(final AtividadeDiaria atividadeDiaria) throws ParseException {

        redirect = "atividades";

        return new ModelAndView("atividade/resultado")
                .addObject("ativs",
                        new AtividadeDiaria())
                .addObject("busca", atividadeDiaria.getDate());
    }

    @GetMapping("/nova")
    public ModelAndView novaAtividade() {
        return new ModelAndView("atividade/nova").addObject("novaAtividade", new AtividadeDiaria());
    }

    @PostMapping("/nova")
    public String novaAtividadePost(AtividadeDiaria atividadeDiaria) throws ParseException {

        repository.save(atividadeDiaria);

        return "redirect:/atividades";
    }

    @GetMapping("/{id}")
    public ModelAndView atividade(@PathVariable long id) {
        return new ModelAndView("atividade/atividade").addObject("ativ", repository.findAtividadeDiariaById(id));
    }

    @GetMapping("/atividade-edit-{id}")
    public ModelAndView atividadeEditar(@PathVariable long id) {

        redirect = "atividades";

        return new ModelAndView("atividade/nova").addObject("novaAtividade", repository.findAtividadeDiariaById(id));
    }

    @PostMapping("/atividade-edit-{id}")
    public String atividadeEditarPost(AtividadeDiaria diaria) {

        repository.save(diaria);

        return "redirect:/atividades";
    }

    @GetMapping("/atividade-elim-{id}")
    public String atividadeEliminar(@PathVariable long id) {

        repository.deleteById(id);

        return "redirect:/voltar";
    }

    @GetMapping("/voltar")
    public String voltar() {
        final String red = "redirect:/" + redirect;

        redirect = "";

        return red;
    }

}
