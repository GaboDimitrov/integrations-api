package com.integrationsapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AppConnectorController {

    @Autowired
    private AppConnectorRepository appConnectorRepository;

    @PostMapping(path = "/connectors")
    public @ResponseBody
    ResponseEntity<AppConnector> addNewAppConnector(@RequestBody AppConnector appConnector) {
        if (appConnector.getName().isEmpty()) {
            throw new ParameterMissingOrEmptyException();
        }

        appConnectorRepository.save(appConnector);
        return new ResponseEntity<AppConnector>(appConnector, HttpStatus.CREATED);
    }

    @GetMapping(path = "/connectors")
    public @ResponseBody
    Iterable<AppConnector> getAllAppConnectors() {
        return appConnectorRepository.findAll();
    }

    @GetMapping(path = "/connectors/{id}")
    public @ResponseBody
    ResponseEntity<AppConnector> getAppConnectorById(@PathVariable(value = "id") Integer id) {
         AppConnector appConnector = appConnectorRepository.findById(id).orElseThrow(AppConnectorNotFoundException::new);
        return ResponseEntity.ok().body(appConnector);
    }

    @PutMapping(path = "/connectors/{id}")
    public @ResponseBody
    ResponseEntity<AppConnector> getAppConnectorById(@PathVariable(value = "id") Integer id, @RequestBody AppConnector newAppConnector) {
        if (newAppConnector.getName().isEmpty()) {
            throw new ParameterMissingOrEmptyException();
        }

        AppConnector appConnector = appConnectorRepository.findById(id).orElseThrow(AppConnectorNotFoundException::new);

        appConnector.setName(newAppConnector.getName());
        appConnector.setDescription(newAppConnector.getDescription());
        appConnector.setIsActive(newAppConnector.getIsActive());

        appConnectorRepository.save(appConnector);
        return ResponseEntity.ok().body(appConnector);
    }


    @PatchMapping(path = "/connectors/{id}")
    public ResponseEntity<AppConnector> updateCustomer(@PathVariable Integer id, @RequestBody Map<Object, Object> fields) {

        if (!fields.containsKey("name") || fields.get("name").toString().isEmpty()) {
            throw new ParameterMissingOrEmptyException();
        }

        AppConnector appConnector = appConnectorRepository.findById(id).orElseThrow(AppConnectorNotFoundException::new);

        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(AppConnector.class, (String) key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, appConnector, value);
        });

        appConnectorRepository.save(appConnector);
        return ResponseEntity.ok().body(appConnector);
    }

    @DeleteMapping("/connectors/{id}")
    public @ResponseBody
    ResponseEntity<String> deleteAppConnector(@PathVariable(value = "id") Integer id) {
        AppConnector appConnector = appConnectorRepository.findById(id).orElseThrow(AppConnectorNotFoundException::new);
        appConnectorRepository.delete(appConnector);
        return ResponseEntity.ok().body("App connector has been deleted successfully.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private void appConnectorNotFoundHandler(AppConnectorNotFoundException ex) {
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private void parameterMissing(ParameterMissingOrEmptyException ex) {
    }

}
