package com.tpe.domain;

import com.tpe.domain.enums.*;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="tbl_role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING) // Tabloya enum int değeri ile değil String ifade ile kaydedilsin
    @Column(length = 30, nullable = false)
    private UserRole name;  // 1 ,2 ,3

    @Override
    public String toString() {
        return "Role{" +
                "name=" + name +
                '}';
    }
}
