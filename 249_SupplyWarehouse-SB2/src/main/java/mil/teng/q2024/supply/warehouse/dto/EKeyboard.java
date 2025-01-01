package mil.teng.q2024.supply.warehouse.dto;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class EKeyboard {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String modelName;
    private String vendor;
    private String description;

    public EKeyboard(EKeyboardResource src) {
        this.modelName = src.getModelName();
        this.vendor = src.getVendor();
        this.description = src.getDescription();
    }
}
