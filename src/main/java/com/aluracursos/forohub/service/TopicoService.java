package com.aluracursos.forohub.service;

import com.aluracursos.forohub.dto.DatosRegistroTopico;
import com.aluracursos.forohub.dto.DatosListadoTopico;
import com.aluracursos.forohub.dto.DatosActualizarTopico;
import com.aluracursos.forohub.model.Topico;
import com.aluracursos.forohub.model.Usuario;
import com.aluracursos.forohub.model.Curso;
import com.aluracursos.forohub.repository.TopicoRepository;
import com.aluracursos.forohub.repository.UsuarioRepository;
import com.aluracursos.forohub.repository.CursoRepository;
import com.aluracursos.forohub.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    public DatosListadoTopico registrarTopico(DatosRegistroTopico datosRegistroTopico) {
        Optional<Usuario> autorOptional = usuarioRepository.findById(datosRegistroTopico.autorId());
        Optional<Curso> cursoOptional = cursoRepository.findById(datosRegistroTopico.cursoId());

        if (autorOptional.isEmpty() || cursoOptional.isEmpty()) {
            throw new ResourceNotFoundException("Autor o curso no encontrado");
        }

        if (topicoRepository.findByTituloAndMensaje(datosRegistroTopico.titulo(), datosRegistroTopico.mensaje())
                .isPresent()) {
            throw new IllegalArgumentException("Ya existe un tópico con el mismo título y mensaje");
        }

        Usuario autor = autorOptional.get();
        Curso curso = cursoOptional.get();

        Topico topico = new Topico(null, datosRegistroTopico.titulo(), datosRegistroTopico.mensaje(),
                LocalDateTime.now(), "ACTIVO", autor, curso);
        topicoRepository.save(topico);
        return new DatosListadoTopico(topico);
    }

    public Page<DatosListadoTopico> listarTopicos(Pageable paginacion) {
        return topicoRepository.findByStatus("ACTIVO", paginacion).map(DatosListadoTopico::new);
    }

    public DatosListadoTopico retornarDatosTopico(Long id) {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tópico no encontrado"));
        return new DatosListadoTopico(topico);
    }

    public DatosListadoTopico actualizarTopico(Long id, DatosActualizarTopico datosActualizarTopico) {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tópico no encontrado"));

        if (datosActualizarTopico.titulo() != null) {
            topico.setTitulo(datosActualizarTopico.titulo());
        }
        if (datosActualizarTopico.mensaje() != null) {
            topico.setMensaje(datosActualizarTopico.mensaje());
        }
        if (datosActualizarTopico.status() != null) {
            topico.setStatus(datosActualizarTopico.status());
        }
        if (datosActualizarTopico.autorId() != null) {
            Optional<Usuario> autorOptional = usuarioRepository.findById(datosActualizarTopico.autorId());
            autorOptional.ifPresent(topico::setAutor);
        }
        if (datosActualizarTopico.cursoId() != null) {
            Optional<Curso> cursoOptional = cursoRepository.findById(datosActualizarTopico.cursoId());
            cursoOptional.ifPresent(topico::setCurso);
        }

        topicoRepository.save(topico);
        return new DatosListadoTopico(topico);
    }

    public void eliminarTopico(Long id) {
        if (!topicoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tópico no encontrado");
        }
        try {
            topicoRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el tópico: " + e.getMessage());
        }
    }
}
