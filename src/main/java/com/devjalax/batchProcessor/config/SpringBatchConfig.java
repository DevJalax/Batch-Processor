package com.devjalax.batchProcessor.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import com.devjalax.batchProcessor.entity.Customer;
import com.devjalax.batchProcessor.repository.CustRepo;

import lombok.AllArgsConstructor;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class SpringBatchConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	private StepBuilderFactory stepBuilderFactory;
	private CustRepo repo;
	
	@Bean
	public FlatFileItemReader<Customer> reader(){
		
		FlatFileItemReader itemReader = new FlatFileItemReader();
		itemReader.setResource(new FileSystemResource("src/main/resources/customers.csv"));
		itemReader.setName("csvReader");
		itemReader.setLinesToSkip(1);
		itemReader.setLineMapper(lineMapper());
		return itemReader;
	}

	private LineMapper<Customer> lineMapper() {
		
		DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
		delimitedLineTokenizer.setDelimiter(",");
		delimitedLineTokenizer.setStrict(false);
		delimitedLineTokenizer.setNames("id","firstname","lastname","email","email2","profession");
		
		BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Customer.class);
		lineMapper.setLineTokenizer(delimitedLineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);

		return lineMapper;

	}
	
	@Bean
	public CustomerProcessor processor() {
		return new CustomerProcessor();
	}
	
	@Bean
	public RepositoryItemWriter<Customer> writer(){
		
		RepositoryItemWriter<Customer> writer = new RepositoryItemWriter<Customer>();
		writer.setRepository(repo);
		writer.setMethodName("save");
		
		return writer;
		
	}
	
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("csv-step").<Customer,Customer>chunk(10)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.taskExecutor(taskExecutor())
				.build();
	}
	
	@Bean
	public Job runjob() {
	 return jobBuilderFactory.get("importCustomers")
			 .flow(step1()).end().build();
	}
	
	public TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
		asyncTaskExecutor.setConcurrencyLimit(10);
		return taskExecutor();
	}
	
}
