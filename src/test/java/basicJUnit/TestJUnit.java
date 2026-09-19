package basicJUnit;

import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestJUnit {

    @BeforeEach
    @Test
    public void crearProyecto(){
        System.out.println("Test Crear");
    }

    @AfterEach
    public void buscarProyecto(){
        System.out.println("Test Buscar");
    }

    @Order(3)
    @Test
    public void eliminarProyecto(){
        System.out.println("Test Eliminar");
    }

}
