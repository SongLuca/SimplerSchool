package main.application.models;

import java.util.List;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;

public class OrarioSettimanale {
	private String nomeOrario;
	private Table<String, String, String> tabellaOrari;

	public OrarioSettimanale() {
		List<String> orarioRowTable = Lists.newArrayList("1ora", "2ora", "3ora", "4ora", "5ora", "6ora", "7ora", "8ora",
				"9ora", "10ora", "11ora");
		List<String> orarioColumnTables = Lists.newArrayList("lunedi", "martedi", "mercoledi", "giovedi", "venerdi",
				"sabato", "domenica");
		tabellaOrari = ArrayTable.create(orarioRowTable, orarioColumnTables);
		tabellaOrari.put("1ora", "lunedi", "italiano");
		System.out.println(tabellaOrari.toString());
	}
}
