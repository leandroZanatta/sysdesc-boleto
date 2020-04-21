package br.com.sysdesc.boletos.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Arrays;

import org.jrimum.texgit.FlatFile;
import org.jrimum.texgit.Record;

import br.com.sysdesc.boletos.util.anotations.XmlAnotation;
import br.com.sysdesc.boletos.util.model.InnerRecordModel;
import br.com.sysdesc.boletos.util.model.Model;
import br.com.sysdesc.boletos.util.model.RecordModel;
import br.com.sysdesc.util.classes.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RecordUtil {

    public static void createRecord(RecordModel model, FlatFile<Record> records) {

        Record record = records.createRecord(model.getRecordName());

        convertModel(model, record, records);

        records.addRecord(record);
    }

    private static void convertModel(Model model, Record record, FlatFile<Record> records) {

        for (Field field : model.getClass().getDeclaredFields()) {

            try {
                if (Arrays.asList(field.getType().getInterfaces()).contains(InnerRecordModel.class)) {

                    createInnerRecord(model, record, records, field);

                } else if (Arrays.asList(field.getType().getInterfaces()).contains(Model.class)) {

                    convertModel((Model) getObject(model, field), record, records);

                } else if (field.isAnnotationPresent(XmlAnotation.class)) {

                    XmlAnotation xmlAnotation = field.getAnnotation(XmlAnotation.class);

                    Object value = getObject(model, field);

                    if (value == null) {
                        System.out.println("CAMPO NULLO");
                    }

                    record.setValue(xmlAnotation.name(), formatValue(value, xmlAnotation));
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {

                log.error("Erro buscando valor do modelo", e);
            }
        }
    }

    private static void createInnerRecord(Model model, Record record, FlatFile<Record> records, Field field) throws IllegalAccessException {

        InnerRecordModel innerModel = (InnerRecordModel) getObject(model, field);

        Record innerRecord = records.createRecord(innerModel.getRecordName());

        convertModel(innerModel, innerRecord, records);

        record.addInnerRecord(innerRecord);
    }

    private static Object getObject(Model model, Field field) throws IllegalAccessException {

        field.setAccessible(true);

        return field.get(model);
    }

    private static Object formatValue(Object value, XmlAnotation xmlAnotation) {

        if (xmlAnotation.type().equals(BigDecimal.class)) {

            BigDecimal valor = ((BigDecimal) value).setScale(xmlAnotation.decimal(), RoundingMode.HALF_EVEN);

            NumberFormat numberFormat = NumberFormat.getNumberInstance();

            numberFormat.setMaximumFractionDigits(xmlAnotation.decimal());
            numberFormat.setMinimumFractionDigits(xmlAnotation.decimal());

            return StringUtil.formatarNumero(numberFormat.format(valor));
        }

        return value;
    }

}
