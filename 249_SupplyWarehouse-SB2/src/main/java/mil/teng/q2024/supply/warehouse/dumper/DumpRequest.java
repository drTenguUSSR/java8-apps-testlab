package mil.teng.q2024.supply.warehouse.dumper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DumpRequest {
    private String url;
    private String query;
    private Map<String,String> headers;
    private String body;
}
