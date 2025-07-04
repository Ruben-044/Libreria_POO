import com.formdev.flatlaf.FlatIntelliJLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
//hola
class Libro {
    String titulo;
    String sinopsis;

    public Libro(String titulo, String sinopsis) {
        this.titulo = titulo;
        this.sinopsis = sinopsis;
    }

    @Override
    public String toString() {
        return titulo;
    }
}

class Categoria {
    String nombre;
    List<Libro> libros;

    public Categoria(String nombre) {
        this.nombre = nombre;
        this.libros = new ArrayList<>();
    }

    public void agregarLibro(Libro libro) {
        libros.add(libro);
    }

    @Override
    public String toString() {
        return nombre;
    }
}

public class Main extends JFrame {
    private JComboBox<Categoria> comboCategorias;
    private JList<Libro> listaLibros;
    private DefaultListModel<Libro> modeloLibros;
    private JTextArea areaSinopsis;

    private JButton btnAgregar, btnEditar, btnEliminar;
    private List<Categoria> categorias;

    public Main() {
        setTitle(" Sistema de Gestión de Libros");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        UIManager.put("Button.arc", 20);
        UIManager.put("Component.arc", 10);

        // Inicializar datos
        categorias = cargarCategorias();

        comboCategorias = new JComboBox<>(categorias.toArray(new Categoria[0]));
        comboCategorias.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboCategorias.addActionListener(e -> cargarLibros());

        modeloLibros = new DefaultListModel<>();
        listaLibros = new JList<>(modeloLibros);
        listaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaLibros.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        areaSinopsis = new JTextArea();
        areaSinopsis.setEditable(false);
        areaSinopsis.setLineWrap(true);
        areaSinopsis.setWrapStyleWord(true);
        areaSinopsis.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        listaLibros.addListSelectionListener(e -> {
            Libro libro = listaLibros.getSelectedValue();
            areaSinopsis.setText(libro != null ? libro.sinopsis : "");
        });

        // Botones con estilo moderno
        btnAgregar = crearBoton(" Agregar Libro");
        btnEditar = crearBoton(" Editar Libro");
        btnEliminar = crearBoton("️ Eliminar Libro");

        btnAgregar.addActionListener(e -> agregarLibro());
        btnEditar.addActionListener(e -> editarLibro());
        btnEliminar.addActionListener(e -> eliminarLibro());

        JPanel panelTop = new JPanel(new BorderLayout(10, 10));
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        panelTop.add(new JLabel("Categoría:", JLabel.LEFT), BorderLayout.WEST);
        panelTop.add(comboCategorias, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(listaLibros), new JScrollPane(areaSinopsis));
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.4);

        add(panelTop, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        cargarLibros();
        setVisible(true);
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(60, 130, 200));
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private List<Categoria> cargarCategorias() {
        List<Categoria> lista = new ArrayList<>();

        Categoria ficcion = new Categoria("Ficción");
        ficcion.agregarLibro(new Libro("Cien años de soledad", "Historia de la familia Buendía en Macondo."));
        ficcion.agregarLibro(new Libro("1984", "Sociedad distópica bajo vigilancia total."));
        lista.add(ficcion);

        Categoria tecnologia = new Categoria("Tecnología");
        tecnologia.agregarLibro(new Libro("Clean Code", "Principios para escribir buen código."));
        lista.add(tecnologia);

        Categoria terror = new Categoria("Terror");
        lista.add(terror);
        return lista;
    }

    private void cargarLibros() {
        Categoria categoria = (Categoria) comboCategorias.getSelectedItem();
        modeloLibros.clear();
        if (categoria != null) {
            for (Libro libro : categoria.libros) {
                modeloLibros.addElement(libro);
            }
        }
        areaSinopsis.setText("");
    }

    private void agregarLibro() {
        Categoria categoria = (Categoria) comboCategorias.getSelectedItem();
        if (categoria == null) return;

        String titulo = JOptionPane.showInputDialog(this, "Título del libro:");
        if (titulo == null || titulo.trim().isEmpty()) return;

        String sinopsis = JOptionPane.showInputDialog(this, "Sinopsis del libro:");
        if (sinopsis == null) sinopsis = "";

        categoria.agregarLibro(new Libro(titulo.trim(), sinopsis.trim()));
        cargarLibros();
    }

    private void editarLibro() {
        Categoria categoria = (Categoria) comboCategorias.getSelectedItem();
        Libro libro = listaLibros.getSelectedValue();
        if (categoria == null || libro == null) return;

        String nuevoTitulo = JOptionPane.showInputDialog(this, "Editar título:", libro.titulo);
        if (nuevoTitulo == null || nuevoTitulo.trim().isEmpty()) return;

        String nuevaSinopsis = JOptionPane.showInputDialog(this, "Editar sinopsis:", libro.sinopsis);
        if (nuevaSinopsis == null) nuevaSinopsis = "";

        libro.titulo = nuevoTitulo.trim();
        libro.sinopsis = nuevaSinopsis.trim();
        cargarLibros();
    }

    private void eliminarLibro() {
        Categoria categoria = (Categoria) comboCategorias.getSelectedItem();
        Libro libro = listaLibros.getSelectedValue();
        if (categoria == null || libro == null) return;

        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar libro seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            categoria.libros.remove(libro);
            cargarLibros();
        }
    }

    public static void main(String[] args) {
        try {
            FlatIntelliJLaf.setup();
        } catch (Exception e) {
            System.out.println("No se pudo aplicar FlatLaf");
        }
        SwingUtilities.invokeLater(Main::new);
    }
}
