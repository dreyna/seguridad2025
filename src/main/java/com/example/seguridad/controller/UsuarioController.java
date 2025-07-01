package com.example.seguridad.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.data.domain.Page;
import com.example.seguridad.entity.Usuario;
import com.example.seguridad.repository.RolRepository;
import com.example.seguridad.service.UsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
	@Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolRepository rolRepository;

    @GetMapping
    public String listarUsuarios(@RequestParam(defaultValue = "") String filtro,
                                 @RequestParam(defaultValue = "0") int page,
                                 Model model) {
        Page<Usuario> pagina = usuarioService.listarUsuariosPaginado(filtro, PageRequest.of(page, 5));
        model.addAttribute("usuarios", pagina.getContent());
        model.addAttribute("totalPages", pagina.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("filtro", filtro);
        return "usuario/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", rolRepository.findAll());
        return "usuario/formulario";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario, Model model) {
        try {
            usuarioService.guardar(usuario);
            return "redirect:/usuarios";
        } catch (IllegalArgumentException e) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("roles", rolRepository.findAll());
            model.addAttribute("error", e.getMessage());
            return "usuario/formulario";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", rolRepository.findAll());
        return "usuario/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return "redirect:/usuarios";
    }
    
    @GetMapping("/buscar")
    @ResponseBody
    public List<Usuario> buscarUsuariosAjax(@RequestParam String term) {
        return usuarioService.buscarPorNombre(term);
    }
}
