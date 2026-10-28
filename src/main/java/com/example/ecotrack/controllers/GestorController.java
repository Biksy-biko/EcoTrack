package com.example.ecotrack.controllers;

import com.example.ecotrack.DTO.AtualizarStatusUsuarioRequest;
import com.example.ecotrack.DTO.AtualizarUsuarioRequest;
import com.example.ecotrack.DTO.AtualizarUsuarioResponse;
import com.example.ecotrack.DTO.UsuarioConsultaResponse;
import com.example.ecotrack.entities.Usuario;
import com.example.ecotrack.repository.EmpresaRepository;
import com.example.ecotrack.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/gestores")
@Tag(name = "Controlador de Usuários", description = "Classe responsável por fazer o controle do CRUD dos Usuários do sistema.")
public class GestorController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @GetMapping
    @Operation(
            summary = "Consultar todos os usuários cadastrados",
            description = "Retorna uma lista de todos os usuários cadastrados no sistema."
    )
    public List<UsuarioConsultaResponse> ConsultaUsuario() {

        return usuarioRepository.findAll().stream().map(UsuarioConsultaResponse::new).toList();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar Usuário por ID",
            description = "Método responsável por fazer a consulta de um único usuário, que recebe como parâmetro o PathVariable 'id', e retorna o usuário associado."
    )
    public ResponseEntity<Usuario> ConsultaUsuarioPorId(@PathVariable Long id) {
        var usuario = usuarioRepository.findById(id).orElse(null);

        if (usuario == null){
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/empresa/{empresaId}")
    @Operation(
            summary = "Consultar usuário por ID da empresa",
            description = "Retorna um objeto de usuário construído com base no ID da empresa fornecido no caminho da URL."
    )
    public Usuario ConsultarIdEmpresa (@PathVariable Long empresaId){
        Usuario usuarioConstrutorCompleto = new Usuario("Samuel","0390213921","27/11/2023");
        return usuarioConstrutorCompleto;
    }

    @PostMapping
    @Operation(
            summary = "Cadastrar um novo usuário",
            description = "Recebe os dados de um usuário e uma empresa válida para realizar o cadastro no sistema. Retorna o ID gerado e uma mensagem de sucesso."
    )
    public ResponseEntity<AtualizarUsuarioResponse> CadastrarUsuario(@RequestBody AtualizarUsuarioRequest usuarioRequest){

        var empresaBanco = empresaRepository.findById(usuarioRequest.getEmpresa_id()).orElse(null);

        if (empresaBanco == null){
            return ResponseEntity.notFound().build();
        }

        Usuario usuariobanco = new Usuario();
        usuariobanco.setNome(usuarioRequest.getNome());
        usuariobanco.setCpf(usuarioRequest.getCpf());
        usuariobanco.setDataNascimento(usuarioRequest.getDataNascimento());
        usuariobanco.setSenha(usuarioRequest.getSenha());
        usuariobanco.setEmpresa(empresaBanco);

        usuariobanco.setDataCadastro(LocalDateTime.now());
        usuariobanco.setStatus("A");

        //Salvando no banco
        usuarioRepository.save(usuariobanco);

        return ResponseEntity.ok(new AtualizarUsuarioResponse(usuariobanco.getId(),"Usuário atualizado com sucesso."));

    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar cadastro de usuário",
            description = "Atualiza os dados cadastrais de um usuário existente com base no ID informado na URL e nos novos dados enviados no corpo da requisição."
    )
    public ResponseEntity<AtualizarUsuarioResponse> AtualizarCadastroUsuario(@PathVariable Long id, @RequestBody AtualizarUsuarioRequest usuarioRequest){

        Usuario usuariobanco = usuarioRepository.findById(id).orElse(null);
        if (usuariobanco != null){

            var empresaBanco = empresaRepository.findById(usuarioRequest.getEmpresa_id()).orElse(null);
            if (empresaBanco == null){
                return ResponseEntity.notFound().build();
            }

            usuariobanco.setEmpresa(empresaBanco);
            usuariobanco.setNome(usuarioRequest.getNome());
            usuariobanco.setCpf(usuarioRequest.getCpf());
            usuariobanco.setDataNascimento(usuarioRequest.getDataNascimento());
            usuariobanco.setDataAtualizacao(LocalDateTime.now());
            usuariobanco.setSenha(usuarioRequest.getSenha());
            usuarioRepository.save(usuariobanco);
            return ResponseEntity.ok(new AtualizarUsuarioResponse(usuariobanco.getId(),"Usuário atualizado com sucesso."));


        }return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Atualizar status do usuário",
            description = "Altera especificamente o campo de status de um usuário existente localizado pelo ID na URL."
    )
    public ResponseEntity<AtualizarUsuarioResponse> AtualizarStatus(@PathVariable Long id, @RequestBody AtualizarStatusUsuarioRequest usuarioRequest){

        Usuario usuariobanco = usuarioRepository.findById(id).orElse(null);

        if (usuariobanco != null){
            usuariobanco.setStatus(usuarioRequest.getStatus());
            usuarioRepository.save(usuariobanco);
            return ResponseEntity.ok(new AtualizarUsuarioResponse(usuariobanco.getId(),"Usuário atualizado com sucesso."));


        }return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Desativar usuário",
            description = "Realiza a exclusão lógica de um usuário no sistema, alterando o seu status para 'D' (Desativado) com base no ID fornecido."
    )
    public ResponseEntity<AtualizarUsuarioResponse> DeleteStatus(@PathVariable Long id){

        Usuario usuariobanco = usuarioRepository.findById(id).orElse(null);

        if (usuariobanco != null){
            usuariobanco.setStatus("D");
            usuarioRepository.save(usuariobanco);
            return ResponseEntity.ok().build();


        }return ResponseEntity.notFound().build();
    }
}
