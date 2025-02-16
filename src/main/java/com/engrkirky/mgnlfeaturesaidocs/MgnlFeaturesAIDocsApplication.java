package com.engrkirky.mgnlfeaturesaidocs;

import com.engrkirky.mgnlfeaturesaidocs.config.HintsRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.shell.command.annotation.CommandScan;

@CommandScan
@ImportRuntimeHints(HintsRegistrar.class)
@SpringBootApplication
public class MgnlFeaturesAIDocsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MgnlFeaturesAIDocsApplication.class, args);
	}

}
