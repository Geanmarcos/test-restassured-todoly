package stepDefinition;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String nombre;
    private String apellidos;
    private String telefono;
    private String direccion;
    private String dni;

}
