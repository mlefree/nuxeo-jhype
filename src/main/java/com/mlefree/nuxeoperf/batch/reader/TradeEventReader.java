package com.mlefree.nuxeoperf.batch.reader;

import com.mlefree.nuxeoperf.batch.model.TradeEvent;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;

public class TradeEventReader extends FlatFileItemReader<TradeEvent> {

	public TradeEventReader() {

		this.setResource(new ClassPathResource("avs-small.gitignored.csv"));
		//Skip the file header line ?
		// this.setLinesToSkip(1);
		//Line is mapped to item (TradeEvent) using setLineMapper(LineMapper)
		this.setLineMapper(new DefaultLineMapper<TradeEvent>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "avs"});
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<TradeEvent>() {
					{
						setTargetType(TradeEvent.class);
					}
				});
			}
		});
	}

}
