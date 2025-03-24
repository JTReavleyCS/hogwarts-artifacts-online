package edu.tcu.cs.hogwartsartifactsonline.wizard;

import edu.tcu.cs.hogwartsartifactsonline.artifact.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.artifact.ArtifactRepository;
import edu.tcu.cs.hogwartsartifactsonline.artifact.utils.IdWorker;
import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class WizardService {

    private final WizardRepository wizardRepository;

    private final ArtifactRepository artifactRepository;

    public WizardService(WizardRepository wizardRepository, ArtifactRepository artifactRepository) {
        this.wizardRepository = wizardRepository;
        this.artifactRepository = artifactRepository;
    }

    public Wizard findById(String wizardId) {
        return this.wizardRepository.findById(wizardId)
                .orElseThrow(() -> new ObjectNotFoundException(wizardId));
    }

    public List<Wizard> findAll() {
        return this.wizardRepository.findAll();
    }

    public Wizard save(Wizard newWizard) {
        newWizard.setId(idWorker.nextId() + "");
        return this.wizardRepository.save(newWizard);
    }

    public Wizard update(String wizardId, Wizard update) {
        return this.wizardRepository.findById(wizardId)
                .map(oldWizard -> {
                    oldWizard.setName(update.getName());
                    oldWizard.setDescription(update.getDescription());
                    oldWizard.setImageUrl(update.getImageUrl());
                    return this.wizardRepository.save(oldWizard);
                })
                .orElseThrow(() -> new WizardNotFoundException(wizardId));
    }

    public void delete(String artifactId) {
        this.wizardRepository.findById(wizardId)
                .orElseThrow(() -> new ObjectNotFoundException(wizardId));
        this.wizardRepository.deleteById(wizardId);
    }

    public void assignArtifact(String wizardId, String artifactId) {
        // find the artifct by id from DB
        Artifact artifactToBeAssigned = this.artifactRepository.findById(artifactId).orElseThrow(() ->
                new ObjectNotFoundException("artifact", artifactId));

        // find wizard by id from DB
        Wizard wizard = this.wizardRepository.findById(wizardId).orElseThrow(() ->
                new ObjectNotFoundException("artifact", artifactId));

        // artifact assignment
        // need to see if the artifact is already owned by some wizard
        if (artifactToBeAssigned.getOwner() != null) {
            artifactToBeAssigned.getOwner().removeArtifact(artifactToBeAssigned);
        }
        wizard.addArtifact(artifactToBeAssigned);
    }

}