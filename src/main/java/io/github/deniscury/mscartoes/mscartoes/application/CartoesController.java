package io.github.deniscury.mscartoes.mscartoes.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.deniscury.mscartoes.mscartoes.application.representation.CartaoSaveRequest;
import io.github.deniscury.mscartoes.mscartoes.application.representation.CartoesPorClienteResponse;
import io.github.deniscury.mscartoes.mscartoes.domain.Cartao;
import io.github.deniscury.mscartoes.mscartoes.domain.ClienteCartao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("cartoes")
@RequiredArgsConstructor
@Slf4j
public class CartoesController {
    private final CartaoService cartaoService;
    private final ClienteCartaoService clienteCartaoService;

    @GetMapping
    public String status() {
        log.info("Obtendo o status do microservice de cartoes");
        return "OK";
    }

    @PostMapping
    public ResponseEntity save(@RequestBody CartaoSaveRequest request) {
        Cartao cartao = request.toModel();

        cartaoService.save(cartao);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> getCartoesRendaAte(@RequestParam("renda") Long renda) {
        List<Cartao> list = cartaoService.getCartoesRendaMenorIgual(renda);

        return ResponseEntity.ok(list);
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<List<CartoesPorClienteResponse>> getCartoesByCliente(@RequestParam("cpf") String cpf) {
        List<ClienteCartao> list = clienteCartaoService.listCartoesByCpf(cpf);
        List<CartoesPorClienteResponse> resultList = list.stream().map(CartoesPorClienteResponse::fromModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(resultList);
    }

}
