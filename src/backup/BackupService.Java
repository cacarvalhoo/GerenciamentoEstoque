package backup;

import model.Produto;
import java.io.*;
import java.util.List;

public class BackupService {

    public void salvarEmArquivo(List<Produto> produtos, String caminho) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminho))) {
            bw.write("=== BACKUP DE ESTOQUE ===\n");
            for (Produto p : produtos) {
                bw.write(p.getCodigo() + ";" + p.getNome() + ";" + p.getQuantidade());
                bw.newLine();
            }
        }
    }
}