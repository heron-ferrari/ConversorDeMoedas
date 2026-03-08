import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        Scanner leitura = new Scanner(System.in);
        String chaveApi = "57dab1af592b153e03576798";
        int opcao = 0;

        String menu = """
                ***********************************************
                Seja bem-vindo ao Conversor de Moedas!
                
                1) Dólar (USD) =>> Real Brasileiro (BRL)
                2) Real Brasileiro (BRL) =>> Dólar (USD)
                3) Euro (EUR) =>> Real Brasileiro (BRL)
                4) Real Brasileiro (BRL) =>> Euro (EUR)
                5) Iene Japonês (JPY) =>> Real Brasileiro (BRL)
                6) Real Brasileiro (BRL) =>> Iene Japonês (JPY)
                7) Sair
                
                Escolha uma opção válida:
                ***********************************************
                """;

        while (opcao != 7) {
            System.out.println(menu);
            opcao = leitura.nextInt();

            if (opcao == 7) break;

            String base = "", alvo = "";

            switch (opcao) {
                case 1 -> { base = "USD"; alvo = "BRL"; }
                case 2 -> { base = "BRL"; alvo = "USD"; }
                case 3 -> { base = "EUR"; alvo = "BRL"; }
                case 4 -> { base = "BRL"; alvo = "EUR"; }
                case 5 -> { base = "JPY"; alvo = "BRL"; }
                case 6 -> { base = "BRL"; alvo = "JPY"; }
                default -> System.out.println("Opção inválida!");
            }

            if (!base.isEmpty()) {
                System.out.println("Digite o valor que deseja converter:");
                double valor = leitura.nextDouble();

                try {
                    String endereco = "https://v6.exchangerate-api.com/v6/" + chaveApi + "/pair/" + base + "/" + alvo + "/" + valor;

                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(endereco)).build();
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    DadosConversao dados = new Gson().fromJson(response.body(), DadosConversao.class);

                    System.out.printf("Valor %.2f [%s] corresponde ao valor final de =>>> %.2f [%s]%n",
                            valor, dados.base_code(), dados.conversion_result(), dados.target_code());

                } catch (Exception e) {
                    System.out.println("Erro ao realizar a conversão: " + e.getMessage());
                }
            }
        }
        System.out.println("Programa finalizado.");
    }
}
