package org.shvetsov.models;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("ELECTRONICS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ElectronicsCharacteristics extends ProductCharacteristics {
    @Column(name = "power")
    private Double power;

    @Column(name = "warranty_months")
    private int warrantyMonths;

    @Column(name = "remote_control")
    private Boolean remoteControl;
}