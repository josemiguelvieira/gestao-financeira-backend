package com.jm.gestao_financeira2.service;

import com.jm.gestao_financeira2.auth.dto.DashboardResponse;
import com.jm.gestao_financeira2.auth.dto.DespesaPorCategoriaDTO;
import com.jm.gestao_financeira2.entity.Usuario;
import com.jm.gestao_financeira2.repository.DespesaRepository;
import com.jm.gestao_financeira2.repository.ReceitaRepository;
import com.jm.gestao_financeira2.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class DashboardService {

    private final UsuarioRepository usuarioRepository;
    private final ReceitaRepository receitaRepository;
    private final DespesaRepository despesaRepository;

    public DashboardService(
            UsuarioRepository usuarioRepository,
            ReceitaRepository receitaRepository,
            DespesaRepository despesaRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.receitaRepository = receitaRepository;
        this.despesaRepository = despesaRepository;
    }

    // =========================
    // 👤 Usuário logado
    // =========================
    private Usuario getUsuarioLogado(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    // =========================
    // 📊 Resumo do mês
    // =========================
    public DashboardResponse obterResumoMes(String email, int mes, int ano) {
        Usuario usuario = getUsuarioLogado(email);

        YearMonth ym = YearMonth.of(ano, mes);
        LocalDate inicio = ym.atDay(1);
        LocalDate fim = ym.atEndOfMonth();

        BigDecimal receitasMes = receitaRepository.somarReceitasNoPeriodo(usuario, inicio, fim);
        BigDecimal despesasMes = despesaRepository.somarDespesasNoPeriodo(usuario, inicio, fim);

        BigDecimal saldoAtual = receitasMes.subtract(despesasMes);

        return new DashboardResponse(saldoAtual, receitasMes, despesasMes);
    }

    // =========================
    // 🥧 Despesas por categoria
    // =========================
    public List<DespesaPorCategoriaDTO> despesasPorCategoria(String email, int mes, int ano) {
        Usuario usuario = getUsuarioLogado(email);

        YearMonth ym = YearMonth.of(ano, mes);
        LocalDate inicio = ym.atDay(1);
        LocalDate fim = ym.atEndOfMonth();

        // repo retorna: [0]=nomeCategoria (String), [1]=total (BigDecimal)
        return despesaRepository.totaisPorCategoriaNoPeriodo(usuario, inicio, fim)
                .stream()
                .map(row -> new DespesaPorCategoriaDTO(
                        (String) row[0],
                        (BigDecimal) row[1]
                ))
                .toList();
    }
}
