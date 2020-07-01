package com.infoworks.lab.app.components.ui;


import com.infoworks.lab.app.components.presenters.PassengerEditor;
import com.infoworks.lab.app.config.DatabaseBootstrap;
import com.infoworks.lab.app.entities.Gender;
import com.infoworks.lab.app.entities.Passenger;
import com.infoworks.lab.app.layouts.AppLayout;
import com.infoworks.lab.app.layouts.RoutePath;
import com.infoworks.lab.components.crud.Configurator;
import com.infoworks.lab.components.crud.Crud;
import com.infoworks.lab.components.crud.components.datasource.DefaultDataSource;
import com.infoworks.lab.components.crud.components.datasource.GridDataSource;
import com.infoworks.lab.components.crud.components.utils.EditorDisplayType;
import com.infoworks.lab.components.db.source.JsqlDataSource;
import com.infoworks.lab.components.db.source.SqlDataSource;
import com.infoworks.lab.components.rest.RestExecutor;
import com.infoworks.lab.components.rest.source.RestDataSource;
import com.infoworks.lab.jsql.DataSourceKey;
import com.infoworks.lab.jsql.ExecutorType;
import com.infoworks.lab.jsql.JsqlConfig;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@Route(value = RoutePath.PASSENGERS_CRUD_VIEW, layout = AppLayout.class)
public class PassengerList extends MainContent {

    public PassengerList(){
        //Create DataSource:
        GridDataSource source = createDataSource(ExecutorType.JPQL);

        Configurator configurator = new Configurator(Passenger.class)
                .setDisplayType(EditorDisplayType.COMBINED)
                .setDataSource(source)
                .setEditor(PassengerEditor.class)
                .setDialog(PassengerEditor.class)
                .setGridPageSize(8);

        Crud crud = new Crud(configurator);
        add(crud);
    }

    private GridDataSource createDataSource(ExecutorType executorType){
        if (executorType == ExecutorType.SQL){
            //Fetching Data From Database:
            //DatabaseBootstrap.createTables();
            GridDataSource source = JsqlDataSource.createDataSource(SqlDataSource.class, executorType);
            return source;
        }else if(executorType == ExecutorType.REST) {
            //Fetching Data From WebService:
            GridDataSource source = JsqlDataSource.createDataSource(RestDataSource.class, executorType);
            //Testing RestExecutor:
            DataSourceKey sourceKey = JsqlConfig.createDataSourceKey("app.db");
            ((RestDataSource) source).setExecutor(new RestExecutor(Passenger.class, sourceKey));
            return source;
        }else{
            //In-Memory DataSource:
            GridDataSource source = new DefaultDataSource();
            getPassengers().stream().forEach(passenger -> source.save(passenger));
            return source;
        }
    }

    private List<Passenger> getPassengers() {
        List<Passenger> personList = new ArrayList<>();
        personList.add(new Passenger("Lucas", Gender.MALE, 68));
        personList.add(new Passenger("Peter", Gender.MALE, 38));
        personList.add(new Passenger("Jack", Gender.MALE, 28));
        personList.add(new Passenger("Samuel", Gender.MALE, 53));
        return personList;
    }
}
