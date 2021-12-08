package com.integrationsapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    private AppConnectorRepository appConnectorRepository;

    @PostMapping(path="/add")
    public @ResponseBody String addNewAppConnector(@RequestParam String name, @RequestParam String description) {
        AppConnector appConnector = new AppConnector();
        appConnector.setName(name);
        appConnector.setDescription(description);

        appConnectorRepository.save(appConnector);
        return "Saved";
    }

    @GetMapping(path="/appConnectors")
    public @ResponseBody Iterable<AppConnector> getAllAppConnectors() {
        // This returns a JSON or XML with the users
        return appConnectorRepository.findAll();
    }

    @GetMapping(path="/appConnectors/{id}")
    public @ResponseBody
    Optional<AppConnector> getAppConnectorById(@PathVariable(value = "id") Integer id) {
        // This returns a JSON or XML with the users
        return appConnectorRepository.findById(id);
    }

    @PutMapping(path="/appConnectors/{id}")
    public @ResponseBody
    ResponseEntity<AppConnector> getAppConnectorById(@PathVariable(value = "id") Integer id, @RequestBody AppConnector newAppConnector) {
        Optional<AppConnector> appConnectorOptional = appConnectorRepository.findById(id);

        if (appConnectorOptional.isPresent()) {
            AppConnector appConnector = appConnectorOptional.get();
            appConnector.setName(newAppConnector.getName());
            appConnector.setDescription(newAppConnector.getDescription());
            appConnector.setIsActive(newAppConnector.getIsActive());

            appConnector = appConnectorRepository.save(appConnector);
            return ResponseEntity.ok().body(appConnector);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // TODO: Add patch request

    @DeleteMapping("/appConnectors/{id}")
    public @ResponseBody
    ResponseEntity<AppConnector> deleteAppConnector(@PathVariable(value = "id") Integer id) {
        Optional<AppConnector> appConnectorOptional = appConnectorRepository.findById(id);

        if (appConnectorOptional.isPresent()) {
            appConnectorRepository.delete(appConnectorOptional.get());
            return new ResponseEntity("App connector with id " + id + " has been deleted successfully.", HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
