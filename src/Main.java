// Importaciones necesarias para la interfaz gráfica y funcionalidades básicas
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Clase que representa un Libro con título y sinopsis
class Libro {
    String titulo;
    String sinopsis;

    // Constructor para crear un nuevo libro
    public Libro(String titulo, String sinopsis) {
        this.titulo = titulo;
        this.sinopsis = sinopsis;
    }

    // Método para mostrar el título en listas y combobox
    @Override
    public String toString() {
        return titulo;
    }
}

// Clase que representa una Categoría con nombre y lista de libros
class Categoria {
    String nombre;
    List<Libro> libros;

    // Constructor para crear una nueva categoría
    public Categoria(String nombre) {
        this.nombre = nombre;
        this.libros = new ArrayList<>();
    }

    // Método para agregar un libro a la categoría
    public void agregarLibro(Libro libro) {
        libros.add(libro);
    }

    // Método para mostrar el nombre en combobox
    @Override
    public String toString() {
        return nombre;
    }
}

// Clase principal que extiende JFrame para la interfaz gráfica
public class Main extends JFrame {
    // Componentes de la interfaz
    private JComboBox<Categoria> comboCategorias;
    private JList<Libro> listaLibros;
    private DefaultListModel<Libro> modeloLibros;
    private DefaultListModel<Libro> modeloFavoritos;
    private JList<Libro> listaFavoritos;
    private JTextArea areaSinopsisCatalogo;   // Área de texto para sinopsis en pestaña Catálogo
    private JTextArea areaSinopsisFavoritos;  // Área de texto para sinopsis en pestaña Favoritos
    private JButton btnFavorito;              // Botón para agregar a favoritos
    private JButton btnEliminarFavorito;      // Boton para eliminar de favoritos
    private JButton btnModo;                  // Botón para cambiar tema claro/oscuro
    private JButton btnAgregarLibro;          // Botón para agregar nuevo libro
    private JButton btnEditarLibro;           // Botón para editar libro existente
    private JButton btnEliminarLibro;         // Botón para eliminar libro
    private List<Categoria> categorias;       // Lista de categorías disponibles
    private List<Libro> favoritos;            // Lista de libros favoritos
    private boolean esModoOscuro = false;     // Bandera para controlar el tema actual
    private JTabbedPane tabs;                 // Panel de pestañas (Catálogo/Favoritos)
    private ImageIcon iconoClaro;             // Ícono para tema claro
    private ImageIcon iconoOscuro;            // Ícono para tema oscuro

    // Constructor principal
    public Main() {
        // Configuración básica de la ventana
        setTitle("Catálogo de Libros");
        setSize(800, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Carga y redimensionamiento de íconos para el botón de tema
        ImageIcon iconoClaroOriginal = new ImageIcon("src/icons/claro.png");
        ImageIcon iconoOscuroOriginal = new ImageIcon("src/icons/oscuro.png");
        Image imgClaro = iconoClaroOriginal.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        Image imgOscuro = iconoOscuroOriginal.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        iconoClaro = new ImageIcon(imgClaro);
        iconoOscuro = new ImageIcon(imgOscuro);

        // Inicialización de listas
        categorias = cargarCategorias();
        favoritos = new ArrayList<>();

        // Configuración del botón para cambiar tema
        btnModo = new JButton(iconoOscuro);
        btnModo.setPreferredSize(new Dimension(30, 30));
        btnModo.setContentAreaFilled(false);
        btnModo.setBorder(BorderFactory.createEmptyBorder());
        btnModo.setToolTipText("Cambiar tema");
        btnModo.addActionListener(e -> cambiarModo());

        // Configuración del combobox de categorías
        comboCategorias = new JComboBox<>(categorias.toArray(new Categoria[0]));
        comboCategorias.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboCategorias.addActionListener(e -> cargarLibros());

        // Configuración de la lista de libros
        modeloLibros = new DefaultListModel<>();
        listaLibros = new JList<>(modeloLibros);
        listaLibros.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Configuración de la lista de favoritos
        modeloFavoritos = new DefaultListModel<>();
        listaFavoritos = new JList<>(modeloFavoritos);
        listaFavoritos.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Configuración del área de sinopsis para Catálogo
        areaSinopsisCatalogo = new JTextArea();
        areaSinopsisCatalogo.setEditable(false);
        areaSinopsisCatalogo.setLineWrap(true);       // Ajuste de línea automático
        areaSinopsisCatalogo.setWrapStyleWord(true);  // Cortar por palabras
        areaSinopsisCatalogo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Configuración del área de sinopsis para Favoritos
        areaSinopsisFavoritos = new JTextArea();
        areaSinopsisFavoritos.setEditable(false);
        areaSinopsisFavoritos.setLineWrap(true);
        areaSinopsisFavoritos.setWrapStyleWord(true);
        areaSinopsisFavoritos.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Listener para mostrar sinopsis al seleccionar libro en Catálogo
        listaLibros.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Libro libro = listaLibros.getSelectedValue();
                areaSinopsisCatalogo.setText(libro != null ? libro.sinopsis : "");
            }
        });

        // Listener para mostrar sinopsis al seleccionar libro en Favoritos
        listaFavoritos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Libro libro = listaFavoritos.getSelectedValue();
                areaSinopsisFavoritos.setText(libro != null ? libro.sinopsis : "");
            }
        });

        // Configuración del botón para agregar a favoritos
        btnFavorito = new JButton("Agregar a Favoritos");
        btnFavorito.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnFavorito.addActionListener(e -> agregarAFavoritos());

        // Botón para eliminar de favoritos
        btnEliminarFavorito = new JButton("Eliminar de Favoritos");
        btnEliminarFavorito.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnEliminarFavorito.addActionListener(e -> eliminarDeFavoritos());

        // Configuración de los nuevos botones para gestión de libros
        btnAgregarLibro = new JButton("Agregar Libro");
        btnAgregarLibro.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnAgregarLibro.addActionListener(e -> agregarLibro());

        btnEditarLibro = new JButton("Editar Libro");
        btnEditarLibro.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnEditarLibro.addActionListener(e -> editarLibro());

        btnEliminarLibro = new JButton("Eliminar Libro");
        btnEliminarLibro.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnEliminarLibro.addActionListener(e -> eliminarLibro());

        // Configuración del panel de pestañas
        tabs = new JTabbedPane();
        tabs.addTab("Catálogo", crearPanelCatalogo());
        tabs.addTab("Favoritos", crearPanelFavoritos());

        // Panel superior con categoría y botón de tema
        JPanel panelTop = new JPanel(new BorderLayout(10, 10));
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelCategoria = new JPanel(new BorderLayout());
        panelCategoria.add(new JLabel("Categoría:"), BorderLayout.WEST);
        panelCategoria.add(comboCategorias, BorderLayout.CENTER);

        JPanel panelTopRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTopRight.add(btnModo);

        panelTop.add(panelCategoria, BorderLayout.CENTER);
        panelTop.add(panelTopRight, BorderLayout.EAST);

        // Agregar componentes a la ventana principal
        add(panelTop, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        // Cargar libros iniciales y mostrar ventana
        cargarLibros();
        setVisible(true);
    }

    // Método para crear el panel del Catálogo
    private JPanel crearPanelCatalogo() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(listaLibros), BorderLayout.CENTER);

        // Panel inferior con sinopsis y botones
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setPreferredSize(new Dimension(0, 200)); // Altura fija de 200px

        panelInferior.add(new JScrollPane(areaSinopsisCatalogo), BorderLayout.CENTER);

        // Panel para los botones (agregar, editar, eliminar y favorito)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelBotones.add(btnAgregarLibro);
        panelBotones.add(btnEditarLibro);
        panelBotones.add(btnEliminarLibro);
        panelBotones.add(btnFavorito);

        panelInferior.add(panelBotones, BorderLayout.SOUTH);
        panel.add(panelInferior, BorderLayout.SOUTH);
        return panel;
    }

    // Método para crear el panel de Favoritos
    private JPanel crearPanelFavoritos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(listaFavoritos), BorderLayout.CENTER);

        // Panel para sinopsis y botón
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setPreferredSize(new Dimension(0, 200));

        panelInferior.add(new JScrollPane(areaSinopsisFavoritos), BorderLayout.CENTER);

        // Panel para el botón (centrado)
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.add(btnEliminarFavorito);
        panelInferior.add(panelBoton, BorderLayout.SOUTH);

        panel.add(panelInferior, BorderLayout.SOUTH);
        return panel;
    }

    // Método para eliminar de favoritos
    private void eliminarDeFavoritos() {
        Libro libro = listaFavoritos.getSelectedValue();
        if (libro != null) {
            favoritos.remove(libro);
            modeloFavoritos.removeElement(libro);
            areaSinopsisFavoritos.setText("");
            JOptionPane.showMessageDialog(this, "Libro eliminado de favoritos");
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un libro para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Método para cambiar entre temas claro/oscuro
    private void cambiarModo() {
        try {
            if (esModoOscuro) {
                UIManager.setLookAndFeel(new FlatIntelliJLaf());
                btnModo.setIcon(iconoOscuro);
            } else {
                UIManager.setLookAndFeel(new FlatDarkLaf());
                btnModo.setIcon(iconoClaro);
            }
            esModoOscuro = !esModoOscuro;
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Método para cargar categorías y libros de ejemplo
    private List<Categoria> cargarCategorias() {
        List<Categoria> lista = new ArrayList<>();

        // Categoría Ficción
        Categoria ficcion = new Categoria("Ficción");
        ficcion.agregarLibro(new Libro("Cien años de soledad", "Historia de la familia Buendía en Macondo."));
        ficcion.agregarLibro(new Libro("1984", "Sociedad distópica bajo vigilancia total."));
        lista.add(ficcion);

        // Categoría Fantasia
        Categoria fantasia = new Categoria("Fantasía");
        fantasia.agregarLibro(new Libro("El Señor de los Anillos", "Un hobbit debe destruir un anillo maligno antes de que caiga en manos del Señor Oscuro Sauron."));
        fantasia.agregarLibro(new Libro("Harry Potter y la piedra filosofal"," Un niño descubre que es un mago y comienza su educación en la escuela Hogwarts."));
        lista.add(fantasia);

        // Categoría Terror
        Categoria terror = new Categoria("Terror");
        terror.agregarLibro(new Libro("It", "Una entidad malévola que se alimenta del terror de los niños."));
        terror.agregarLibro(new Libro("El exorcista","Una niña es poseída por un demonio, y dos sacerdotes intentan salvarla mediante un exorcismo."));
        terror.agregarLibro(new Libro("Frankenstein","Un científico crea una criatura con partes de cadáveres, pero su creación se rebela contra él."));
        lista.add(terror);

        // Categoría Romance
        Categoria romance = new Categoria("Romance");
        romance.agregarLibro(new Libro("Orgullo y prejuicio", "Elizabeth Bennet y el señor Darcy superan sus diferencias sociales y personales para encontrar el amor."));
        romance.agregarLibro(new Libro("Cumbres Borrascosas", "Una historia de amor obsesivo y venganza en los sombríos páramos de Yorkshire."));
        romance.agregarLibro(new Libro("Bajo la misma estrella","Dos adolescentes con cáncer se enamoran y buscan darle sentido a sus vidas."));
        lista.add(romance);

        return lista;
    }

    // Método para cargar libros según categoría seleccionada
    private void cargarLibros() {
        Categoria categoria = (Categoria) comboCategorias.getSelectedItem();
        modeloLibros.clear();
        if (categoria != null) {
            for (Libro libro : categoria.libros) {
                modeloLibros.addElement(libro);
            }
        }
        areaSinopsisCatalogo.setText("");
    }

    // Método para agregar libro a favoritos
    private void agregarAFavoritos() {
        Libro libro = listaLibros.getSelectedValue();
        if (libro != null && !favoritos.contains(libro)) {
            favoritos.add(libro);
            modeloFavoritos.addElement(libro);
            JOptionPane.showMessageDialog(this, "Libro agregado a favoritos");
        }
    }

    // Método para agregar un nuevo libro
    private void agregarLibro() {
        Categoria categoriaActual = (Categoria) comboCategorias.getSelectedItem();
        if (categoriaActual == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una categoría primero", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JTextField tituloField = new JTextField();
        JTextArea sinopsisArea = new JTextArea(5, 20);
        sinopsisArea.setLineWrap(true);
        sinopsisArea.setWrapStyleWord(true);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel("Título:"), BorderLayout.NORTH);
        panel.add(tituloField, BorderLayout.CENTER);
        panel.add(new JLabel("Sinopsis:"), BorderLayout.WEST);
        panel.add(new JScrollPane(sinopsisArea), BorderLayout.SOUTH);

        int result = JOptionPane.showConfirmDialog(this, panel, "Agregar Nuevo Libro",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String titulo = tituloField.getText().trim();
            String sinopsis = sinopsisArea.getText().trim();

            if (!titulo.isEmpty()) {
                Libro nuevoLibro = new Libro(titulo, sinopsis);
                categoriaActual.agregarLibro(nuevoLibro);
                modeloLibros.addElement(nuevoLibro);
                JOptionPane.showMessageDialog(this, "Libro agregado exitosamente");
            } else {
                JOptionPane.showMessageDialog(this, "El título no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Método para editar un libro existente
    private void editarLibro() {
        Libro libroSeleccionado = listaLibros.getSelectedValue();
        if (libroSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un libro para editar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JTextField tituloField = new JTextField(libroSeleccionado.titulo);
        JTextArea sinopsisArea = new JTextArea(libroSeleccionado.sinopsis, 5, 20);
        sinopsisArea.setLineWrap(true);
        sinopsisArea.setWrapStyleWord(true);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel("Título:"), BorderLayout.NORTH);
        panel.add(tituloField, BorderLayout.CENTER);
        panel.add(new JLabel("Sinopsis:"), BorderLayout.WEST);
        panel.add(new JScrollPane(sinopsisArea), BorderLayout.SOUTH);

        int result = JOptionPane.showConfirmDialog(this, panel, "Editar Libro",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String nuevoTitulo = tituloField.getText().trim();
            String nuevaSinopsis = sinopsisArea.getText().trim();

            if (!nuevoTitulo.isEmpty()) {
                libroSeleccionado.titulo = nuevoTitulo;
                libroSeleccionado.sinopsis = nuevaSinopsis;

                // Actualizar la lista y favoritos si está allí
                modeloLibros.set(modeloLibros.indexOf(libroSeleccionado), libroSeleccionado);

                if (favoritos.contains(libroSeleccionado)) {
                    int index = favoritos.indexOf(libroSeleccionado);
                    favoritos.set(index, libroSeleccionado);
                    modeloFavoritos.set(index, libroSeleccionado);
                }

                JOptionPane.showMessageDialog(this, "Libro actualizado exitosamente");
            } else {
                JOptionPane.showMessageDialog(this, "El título no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Método para eliminar un libro
    private void eliminarLibro() {
        Libro libroSeleccionado = listaLibros.getSelectedValue();
        if (libroSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un libro para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de eliminar el libro '" + libroSeleccionado.titulo + "'?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            Categoria categoriaActual = (Categoria) comboCategorias.getSelectedItem();
            categoriaActual.libros.remove(libroSeleccionado);
            modeloLibros.removeElement(libroSeleccionado);

            // Eliminar también de favoritos si está allí
            if (favoritos.contains(libroSeleccionado)) {
                favoritos.remove(libroSeleccionado);
                modeloFavoritos.removeElement(libroSeleccionado);
            }

            JOptionPane.showMessageDialog(this, "Libro eliminado exitosamente");
        }
    }

    // Método principal
    public static void main(String[] args) {
        try {
            // Configurar look and feel FlatLaf (tema claro por defecto)
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (Exception e) {
            System.out.println("No se pudo aplicar FlatLaf");
        }
        // Ejecutar la interfaz gráfica en el hilo de eventos
        SwingUtilities.invokeLater(Main::new);
    }
}