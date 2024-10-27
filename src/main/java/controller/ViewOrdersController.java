/*
package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Order;
import model.User;
import util.OrderRepository;

import java.time.format.DateTimeFormatter;

public class ViewOrdersController {

    @FXML
    private TableView<Order> ordersTableView;

    @FXML
    private TableColumn<Order, Integer> orderIdColumn;

    @FXML
    private TableColumn<Order, String> orderDateColumn;

    @FXML
    private TableColumn<Order, Double> totalPriceColumn;

    private User currentUser;
    private OrderRepository orderRepository = new OrderRepository();
    private ObservableList<Order> orderList;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        loadOrders();
    }

    @FXML
    public void initialize() {
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        orderDateColumn.setCellValueFactory(cellData ->
                cellData.getValue().getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
    }

    private void loadOrders() {
        if (currentUser != null) {
            orderList = FXCollections.observableArrayList(orderRepository.getOrdersByUserId(currentUser.getId()));
            ordersTableView.setItems(orderList);
        }
    }
}
*/
