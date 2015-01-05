/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sqoop.connector.idf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.apache.sqoop.connector.common.SqoopIDFUtils.*;
import static org.apache.sqoop.connector.common.TestSqoopIDFUtils.getByteFieldString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.sqoop.common.SqoopException;
import org.apache.sqoop.schema.Schema;
import org.apache.sqoop.schema.type.Array;
import org.apache.sqoop.schema.type.Binary;
import org.apache.sqoop.schema.type.Bit;
import org.apache.sqoop.schema.type.Date;
import org.apache.sqoop.schema.type.DateTime;
import org.apache.sqoop.schema.type.Decimal;
import org.apache.sqoop.schema.type.FixedPoint;
import org.apache.sqoop.schema.type.Text;
import org.apache.sqoop.schema.type.Time;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

public class TestCSVIntermediateDataFormat {

  private CSVIntermediateDataFormat dataFormat;

  @Before
  public void setUp() {
    dataFormat = new CSVIntermediateDataFormat();
  }


  //**************test cases for null and empty input*******************

  @Test
  public void testNullInputAsCSVTextInObjectArrayOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new FixedPoint("1"))
        .addColumn(new FixedPoint("2"))
        .addColumn(new Text("3"))
        .addColumn(new Text("4"))
        .addColumn(new Binary("5"))
        .addColumn(new Text("6"));
    dataFormat.setSchema(schema);
    dataFormat.setCSVTextData(null);
    Object[] out = dataFormat.getObjectData();
    assertNull(out);
  }

  @Test(expected = SqoopException.class)
  public void testEmptyInputAsCSVTextInObjectArrayOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new FixedPoint("1")).addColumn(new FixedPoint("2")).addColumn(new Text("3")).addColumn(new Text("4"))
        .addColumn(new Binary("5")).addColumn(new Text("6"));
    dataFormat.setSchema(schema);
    dataFormat.setCSVTextData("");
    dataFormat.getObjectData();
  }

  @Test
  public void testNullValueAsObjectArrayInAndCSVTextOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new FixedPoint("1")).addColumn(new Decimal("2")).addColumn(new Text("3"))
        .addColumn(new Array("4", new Text("t"))).addColumn(new Binary("5"))
        .addColumn(new org.apache.sqoop.schema.type.Map("6", new Text("t1"), new Text("t2"))).addColumn(new Bit("7"))
        .addColumn(new org.apache.sqoop.schema.type.DateTime("8", false, false))
        .addColumn(new org.apache.sqoop.schema.type.Time("9", false)).addColumn(new org.apache.sqoop.schema.type.Date("10"))
        .addColumn(new org.apache.sqoop.schema.type.FloatingPoint("11"))
        .addColumn(new org.apache.sqoop.schema.type.Set("12", new Text("t4")))
        .addColumn(new org.apache.sqoop.schema.type.Enum("13")).addColumn(new org.apache.sqoop.schema.type.Unknown("14"));

    dataFormat.setSchema(schema);
    Object[] in = { null, null, null, null, null, null, null, null, null, null, null, null, null, null };
    dataFormat.setObjectData(in);

    String csvText = dataFormat.getCSVTextData();
    String[] textValues = csvText.split(",");
    for (String text : textValues) {
      assertEquals(text, NULL_VALUE);
    }
  }

  @Test
  public void testNullValueAsObjectArrayInAndObjectArrayOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new FixedPoint("1")).addColumn(new Decimal("2")).addColumn(new Text("3"))
        .addColumn(new Array("4", new Text("t"))).addColumn(new Binary("5"))
        .addColumn(new org.apache.sqoop.schema.type.Map("6", new Text("t1"), new Text("t2"))).addColumn(new Bit("7"))
        .addColumn(new org.apache.sqoop.schema.type.DateTime("8", false, false))
        .addColumn(new org.apache.sqoop.schema.type.Time("9", false)).addColumn(new org.apache.sqoop.schema.type.Date("10"))
        .addColumn(new org.apache.sqoop.schema.type.FloatingPoint("11"))
        .addColumn(new org.apache.sqoop.schema.type.Set("12", new Text("t4")))
        .addColumn(new org.apache.sqoop.schema.type.Enum("13")).addColumn(new org.apache.sqoop.schema.type.Unknown("14"));

    dataFormat.setSchema(schema);
    Object[] in = { null, null, null, null, null, null, null, null, null, null, null, null, null, null };
    dataFormat.setObjectData(in);

    Object[] out = dataFormat.getObjectData();
    for (Object obj : out) {
      assertEquals(obj, null);
    }
  }

  @Test
  public void testNullValueAsCSVTextInAndObjectArrayOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new FixedPoint("1")).addColumn(new Decimal("2")).addColumn(new Text("3"))
        .addColumn(new Array("4", new Text("t"))).addColumn(new Binary("5"))
        .addColumn(new org.apache.sqoop.schema.type.Map("6", new Text("t1"), new Text("t2"))).addColumn(new Bit("7"))
        .addColumn(new org.apache.sqoop.schema.type.DateTime("8", false, false))
        .addColumn(new org.apache.sqoop.schema.type.Time("9", false)).addColumn(new org.apache.sqoop.schema.type.Date("10"))
        .addColumn(new org.apache.sqoop.schema.type.FloatingPoint("11"))
        .addColumn(new org.apache.sqoop.schema.type.Set("12", new Text("t4")))
        .addColumn(new org.apache.sqoop.schema.type.Enum("13")).addColumn(new org.apache.sqoop.schema.type.Unknown("14"));

    dataFormat.setSchema(schema);
    String[] test = { "NULL", "NULL", "NULL", "NULL", "NULL", "NULL", "NULL", "NULL", "NULL", "NULL", "NULL", "NULL", "NULL",
        "NULL" };
    dataFormat.setCSVTextData(StringUtils.join(test, ","));

    Object[] out = dataFormat.getObjectData();
    for (Object obj : out) {
      assertEquals(obj, null);
    }
  }

  @Test
  public void testNullValueAsCSVTextInAndCSVTextOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new FixedPoint("1")).addColumn(new Decimal("2")).addColumn(new Text("3"))
        .addColumn(new Array("4", new Text("t"))).addColumn(new Binary("5"))
        .addColumn(new org.apache.sqoop.schema.type.Map("6", new Text("t1"), new Text("t2"))).addColumn(new Bit("7"))
        .addColumn(new org.apache.sqoop.schema.type.DateTime("8", false, false))
        .addColumn(new org.apache.sqoop.schema.type.Time("9", false)).addColumn(new org.apache.sqoop.schema.type.Date("10"))
        .addColumn(new org.apache.sqoop.schema.type.FloatingPoint("11"))
        .addColumn(new org.apache.sqoop.schema.type.Set("12", new Text("t4")))
        .addColumn(new org.apache.sqoop.schema.type.Enum("13")).addColumn(new org.apache.sqoop.schema.type.Unknown("14"));

    dataFormat.setSchema(schema);
    String[] test = { "NULL", "NULL", "NULL", "NULL", "NULL", "NULL", "NULL", "NULL", "NULL", "NULL", "NULL", "NULL", "NULL",
        "NULL" };
    dataFormat.setCSVTextData(StringUtils.join(test, ","));

    String csvText = dataFormat.getCSVTextData();
    String[] textValues = csvText.split(",");
    for (String text : textValues) {
      assertEquals(text, NULL_VALUE);
    }
  }

  //**************test cases for primitive types( text, number, bytearray)*******************

  @Test
  public void testInputAsCSVTextInCSVTextOut() {
    String testData = "'ENUM',10,34,'54','random data'," + getByteFieldString(new byte[] { (byte) -112, (byte) 54})
      + ",'" + String.valueOf(0x0A) + "'";
    dataFormat.setCSVTextData(testData);
    assertEquals(testData, dataFormat.getCSVTextData());
  }

  @Test
  public void testInputAsCSVTextInObjectOut() {

    //byte[0] = -112, byte[1] = 54 - 2's complements
    String testData = "10,34,'54','random data'," + getByteFieldString(new byte[] { (byte) -112, (byte) 54})
      + ",'\\n','TEST_ENUM'";
    Schema schema = new Schema("test");
    schema.addColumn(new FixedPoint("1"))
        .addColumn(new FixedPoint("2"))
        .addColumn(new Text("3"))
        .addColumn(new Text("4"))
        .addColumn(new Binary("5"))
        .addColumn(new Text("6"))
        .addColumn(new org.apache.sqoop.schema.type.Enum("7"));

    dataFormat.setSchema(schema);
    dataFormat.setCSVTextData(testData);

    Object[] out = dataFormat.getObjectData();

    assertEquals(new Long(10),out[0]);
    assertEquals(new Long(34),out[1]);
    assertEquals("54",out[2]);
    assertEquals("random data",out[3]);
    assertEquals(-112, ((byte[]) out[4])[0]);
    assertEquals(54, ((byte[])out[4])[1]);
    assertEquals("\n", out[5].toString());
    assertEquals("TEST_ENUM", out[6].toString());
  }

  @Test
  public void testInputAsObjectArayInCSVTextOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new FixedPoint("1"))
        .addColumn(new FixedPoint("2"))
        .addColumn(new Text("3"))
        .addColumn(new Text("4"))
        .addColumn(new Binary("5"))
        .addColumn(new Text("6"))
        .addColumn(new org.apache.sqoop.schema.type.Enum("7"));
    dataFormat.setSchema(schema);

    byte[] byteFieldData = new byte[] { (byte) 0x0D, (byte) -112, (byte) 54};
    Object[] in = new Object[7];
    in[0] = new Long(10);
    in[1] = new Long(34);
    in[2] = "54";
    in[3] = "random data";
    in[4] = byteFieldData;
    in[5] = new String(new char[] { 0x0A });
    in[6] = "TEST_ENUM";

    dataFormat.setObjectData(in);

    //byte[0] = \r byte[1] = -112, byte[1] = 54 - 2's complements
    String testData = "10,34,'54','random data'," + getByteFieldString(byteFieldData).replaceAll("\r", "\\\\r")
        + ",'\\n','TEST_ENUM'";
    assertEquals(testData, dataFormat.getCSVTextData());
  }

  @Test
  public void testObjectArrayInObjectArrayOut() {
    //Test escapable sequences too.
    //byte[0] = -112, byte[1] = 54 - 2's complements
    Schema schema = new Schema("test");
    schema.addColumn(new FixedPoint("1"))
        .addColumn(new FixedPoint("2"))
        .addColumn(new Text("3"))
        .addColumn(new Text("4"))
        .addColumn(new Binary("5"))
        .addColumn(new Text("6"))
        .addColumn(new org.apache.sqoop.schema.type.Enum("7"));

    dataFormat.setSchema(schema);

    Object[] in = new Object[7];
    in[0] = new Long(10);
    in[1] = new Long(34);
    in[2] = "54";
    in[3] = "random data";
    in[4] = new byte[] { (byte) -112, (byte) 54};
    in[5] = new String(new char[] { 0x0A });
    in[6] = "TEST_ENUM";
    Object[] inCopy = new Object[7];
    System.arraycopy(in,0,inCopy,0,in.length);

    // Modifies the input array, so we use the copy to confirm
    dataFormat.setObjectData(in);

    assertTrue(Arrays.deepEquals(inCopy, dataFormat.getObjectData()));
  }

  @Test
  public void testObjectArrayWithNullInCSVTextOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new FixedPoint("1"))
        .addColumn(new FixedPoint("2"))
        .addColumn(new Text("3"))
        .addColumn(new Text("4"))
        .addColumn(new Binary("5"))
        .addColumn(new Text("6"))
        .addColumn(new org.apache.sqoop.schema.type.Enum("7"));

    dataFormat.setSchema(schema);

    byte[] byteFieldData = new byte[] { (byte) 0x0D, (byte) -112, (byte) 54};
    Object[] in = new Object[7];
    in[0] = new Long(10);
    in[1] = new Long(34);
    in[2] = null;
    in[3] = "random data";
    in[4] = byteFieldData;
    in[5] = new String(new char[] { 0x0A });
    in[6] = "TEST_ENUM";

    dataFormat.setObjectData(in);

    //byte[0] = \r byte[1] = -112, byte[1] = 54 - 2's complements
    String testData = "10,34,NULL,'random data'," + getByteFieldString(byteFieldData).replaceAll("\r", "\\\\r")
        + ",'\\n','TEST_ENUM'";
    assertEquals(testData, dataFormat.getCSVTextData());
  }

  @Test
  public void testStringFullRangeOfCharacters() {
    Schema schema = new Schema("test");
    schema.addColumn(new Text("1"));

    dataFormat.setSchema(schema);

    char[] allCharArr = new char[256];
    for(int i = 0; i < allCharArr.length; ++i) {
      allCharArr[i] = (char)i;
    }
    String strData = new String(allCharArr);

    Object[] in = {strData};
    Object[] inCopy = new Object[1];
    System.arraycopy(in, 0, inCopy, 0, in.length);

    // Modifies the input array, so we use the copy to confirm
    dataFormat.setObjectData(in);

    assertEquals(strData, dataFormat.getObjectData()[0]);
    assertTrue(Arrays.deepEquals(inCopy, dataFormat.getObjectData()));
  }

  @Test
  public void testByteArrayFullRangeOfCharacters() {
    Schema schema = new Schema("test");
    schema.addColumn(new Binary("1"));
    dataFormat.setSchema(schema);

    byte[] allCharByteArr = new byte[256];
    for (int i = 0; i < allCharByteArr.length; ++i) {
      allCharByteArr[i] = (byte) i;
    }

    Object[] in = {allCharByteArr};
    Object[] inCopy = new Object[1];
    System.arraycopy(in, 0, inCopy, 0, in.length);

    // Modifies the input array, so we use the copy to confirm
    dataFormat.setObjectData(in);
    assertTrue(Arrays.deepEquals(inCopy, dataFormat.getObjectData()));
  }

  // **************test cases for time*******************

  @Test
  public void testTimeWithCSVTextInCSVTextOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new Time("1", false));
    dataFormat.setSchema(schema);
    dataFormat.setCSVTextData("'12:00:00'");
    assertEquals("'12:00:00'", dataFormat.getCSVTextData());
  }

  @Test
  public void testTimeWithCSVTextInObjectArrayOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new Time("1", false));
    dataFormat.setSchema(schema);
    dataFormat.setCSVTextData("'12:59:59'");
    org.joda.time.LocalTime time = new org.joda.time.LocalTime(12, 59, 59);
    assertEquals(time.toString(), dataFormat.getObjectData()[0].toString());
  }

  @Test
  public void testTimeWithObjectArrayInCSVTextOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new Time("1", true)).addColumn(new Text("2"));
    dataFormat.setSchema(schema);
    org.joda.time.LocalTime time = new org.joda.time.LocalTime(15, 0, 0);
    Object[] in = { time, "test" };
    dataFormat.setObjectData(in);
    assertEquals("'15:00:00.000000','test'", dataFormat.getCSVTextData());
  }

  @Test
  public void testTimeWithObjectArrayInObjectArrayOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new Time("1", true));
    dataFormat.setSchema(schema);
    org.joda.time.LocalTime time = new org.joda.time.LocalTime(2, 23, 33);
    Object[] in = { time };
    dataFormat.setObjectData(in);
    assertEquals(time.toString(), dataFormat.getObjectData()[0].toString());
  }

  // **************test cases for date*******************

  @Test
  public void testDateWithCSVTextInCSVTextOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new Date("1"));
    dataFormat.setSchema(schema);
    dataFormat.setCSVTextData("'2014-10-01'");
    assertEquals("'2014-10-01'", dataFormat.getCSVTextData());
  }

  @Test
  public void testDateWithCSVTextInObjectArrayOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new Date("1"));
    dataFormat.setSchema(schema);
    dataFormat.setCSVTextData("'2014-10-01'");
    org.joda.time.LocalDate date = new org.joda.time.LocalDate(2014, 10, 01);
    assertEquals(date.toString(), dataFormat.getObjectData()[0].toString());
  }

  @Test
  public void testDateWithObjectArrayInCSVTextOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new Date("1")).addColumn(new Text("2"));
    dataFormat.setSchema(schema);
    org.joda.time.LocalDate date = new org.joda.time.LocalDate(2014, 10, 01);
    Object[] in = { date, "test" };
    dataFormat.setObjectData(in);
    assertEquals("'2014-10-01','test'", dataFormat.getCSVTextData());
  }

  @Test
  public void testDateWithObjectArrayInObjectArrayOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new Date("1"));
    dataFormat.setSchema(schema);
    org.joda.time.LocalDate date = new org.joda.time.LocalDate(2014, 10, 01);
    Object[] in = { date };
    dataFormat.setObjectData(in);
    assertEquals(date.toString(), dataFormat.getObjectData()[0].toString());
  }

  // **************test cases for dateTime*******************

  @Test
  public void testDateTimeWithCSVTextInCSVTextOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new DateTime("1", false, false));
    dataFormat.setSchema(schema);

    dataFormat.setCSVTextData("'2014-10-01 12:00:00'");
    assertEquals("'2014-10-01 12:00:00'", dataFormat.getCSVTextData());
  }

  @Test
  public void testDateTimeWithFractionNoTimezoneWithCSVTextInCSVTextOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new DateTime("1", true, false));
    dataFormat.setSchema(schema);
    dataFormat.setCSVTextData("'2014-10-01 12:00:00.000'");
    assertEquals("'2014-10-01 12:00:00.000'", dataFormat.getCSVTextData());
  }

  public void testDateTimeNoFractionNoTimezoneWithCSVTextInObjectArrayOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new DateTime("1", false, false));
    dataFormat.setSchema(schema);
    dataFormat.setCSVTextData("'2014-10-01 12:00:00'");
    // NOTE: string representation will have the T added, it is an
    // implementation quirk of using JODA
    assertEquals("2014-10-01T12:00:00", dataFormat.getObjectData()[0].toString());
  }

  @Test
  public void testDateTimeWithFractionNoTimezoneWithCSVTextInObjectArrayOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new DateTime("1", true, false));
    dataFormat.setSchema(schema);
    dataFormat.setCSVTextData("'2014-10-01 12:00:00.000'");
    // NOTE: string representation will have the T added, it is an
    // implementation quirk of using JODA
    assertEquals("2014-10-01T12:00:00.000", dataFormat.getObjectData()[0].toString());
  }

  // since date is not quoted
  @Test(expected = Exception.class)
  public void testDateTimeNoQuotesWithFractionTimezoneWithCSVTextInObjectArrayOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new DateTime("1", true, true));
    dataFormat.setSchema(schema);
    DateTimeZone zone = DateTimeZone.forID("America/New_York");
    org.joda.time.DateTime dateTime = new org.joda.time.DateTime(zone);
    dataFormat.setCSVTextData(dateTime.toString());
    dataFormat.getObjectData()[0].toString();
  }

  // since date is not in expected format
  @Test(expected = Exception.class)
  public void testDateTimeIncorrectFormatWithCSVTextInObjectArrayOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new DateTime("1", true, true));
    dataFormat.setSchema(schema);
    dataFormat.setCSVTextData("'2014-3310-01 12:00:00.000'");
    dataFormat.getObjectData()[0].toString();
  }

  @Test
  public void testCurrentDateTime2WithFractionNoTimezoneWithCSVTextInObjectArrayOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new DateTime("1", true, false));
    dataFormat.setSchema(schema);
    // current date time
    org.joda.time.DateTime dateTime = new org.joda.time.DateTime();
    String dateTimeString = dtfWithFractionNoTimeZone.print(dateTime);
    dataFormat.setCSVTextData("'" + dateTimeString + "'");
    assertEquals(dateTimeString.replace(" ", "T"), dataFormat.getObjectData()[0].toString());
  }

  @Test
  public void testDateTimeWithFractionAndTimeZoneWithCSVTextInObjectArrayOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new DateTime("1", true, true));
    dataFormat.setSchema(schema);
    dataFormat.setCSVTextData("'2014-10-01 12:00:00.000-0400'");
    // NOTE: string representation will have the T added, it is an
    // implementation quirk of using JODA
    assertEquals("2014-10-01T12:00:00.000-04:00", dataFormat.getObjectData()[0].toString());
  }

  @Test
  public void testDateTimeWithFractionAndTimeZoneObjectInCSVTextOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new DateTime("1", true, true));
    dataFormat.setSchema(schema);
    DateTimeZone zone = DateTimeZone.forID("America/New_York");
    org.joda.time.DateTime dateTime = new org.joda.time.DateTime(2014, 10, 01, 12, 0, 0, 1, zone);
    Object[] in = { dateTime };
    dataFormat.setObjectData(in);
    // Note: DateTime has the timezone info
    assertEquals("'2014-10-01 12:00:00.001-0400'", dataFormat.getCSVTextData());
  }

  @Test
  public void testLocalDateTimeWithObjectInCSVTextOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new DateTime("1", true, false));
    dataFormat.setSchema(schema);
    org.joda.time.LocalDateTime dateTime = new org.joda.time.LocalDateTime(2014, 10, 01, 12, 0, 0, 2);
    Object[] in = { dateTime };
    dataFormat.setObjectData(in);
    // Note: LocalDateTime will not have the timezone info
    assertEquals("'2014-10-01 12:00:00.002'", dataFormat.getCSVTextData());
  }

  @Test
  public void testDateTimeFractionAndTimezoneWithCSVTextInObjectArrayOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new DateTime("1", true, true));
    dataFormat.setSchema(schema);
    dataFormat.setCSVTextData("'2014-10-01 12:00:00.000-04:00'");
    DateTimeZone zone = DateTimeZone.forID("America/New_York");
    org.joda.time.DateTime edateTime = new org.joda.time.DateTime(2014, 10, 01, 12, 0, 0, 0, zone);
    org.joda.time.DateTime dateTime = (org.joda.time.DateTime) dataFormat.getObjectData()[0];
    assertEquals(edateTime.toString(), dateTime.toString());
    // NOTE: string representation will have the T added, it is an
    // implementation quirk of using JODA
    assertEquals("2014-10-01T12:00:00.000-04:00", dataFormat.getObjectData()[0].toString());
  }

  // **************test cases for BIT*******************

  // **************test cases for BIT*******************

  @Test
  public void testBitTrueFalseWithCSVTextInAndCSVTextOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new Bit("1"));
    dataFormat.setSchema(schema);

    for (String trueBit : new String[] { "true", "TRUE" }) {
      dataFormat.setCSVTextData(trueBit);
      assertTrue(Boolean.valueOf(dataFormat.getCSVTextData()));
    }

    for (String falseBit : new String[] { "false", "FALSE" }) {
      dataFormat.setCSVTextData(falseBit);
      assertFalse(Boolean.valueOf(dataFormat.getCSVTextData()));
    }
  }

  @Test
  public void testBitWithCSVTextInAndCSVTextOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new Bit("1"));
    dataFormat.setSchema(schema);
    dataFormat.setCSVTextData("1");
    assertEquals("1", dataFormat.getCSVTextData());
    dataFormat.setCSVTextData("0");
    assertEquals("0", dataFormat.getCSVTextData());
  }

  @Test
  public void testBitWithObjectArrayInAndCSVTextOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new Bit("1")).addColumn(new Bit("2"));
    dataFormat.setSchema(schema);
    Object[] data = new Object[2];
    data[0] = Boolean.TRUE;
    data[1] = Boolean.FALSE;
    dataFormat.setObjectData(data);
    assertEquals("true,false", dataFormat.getCSVTextData());
  }

  @Test(expected = SqoopException.class)
  public void testUnsupportedBitWithObjectArrayInAndCSVTextOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new Bit("1")).addColumn(new Bit("2"));
    dataFormat.setSchema(schema);
    Object[] data = new Object[2];
    data[0] = "1";
    data[1] = "2";
    dataFormat.setObjectData(data);
    assertEquals("1,2", dataFormat.getCSVTextData());
  }

  @Test
  public void testBitWithObjectArrayInAndObjectOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new Bit("1")).addColumn(new Bit("2"));
    dataFormat.setSchema(schema);
    Object[] data = new Object[2];
    data[0] = Boolean.TRUE;
    data[1] = Boolean.FALSE;
    dataFormat.setObjectData(data);
    assertEquals(true, dataFormat.getObjectData()[0]);
    assertEquals(false, dataFormat.getObjectData()[1]);
    data[0] = "1";
    data[1] = "0";
    dataFormat.setObjectData(data);
    assertEquals(true, dataFormat.getObjectData()[0]);
    assertEquals(false, dataFormat.getObjectData()[1]);
  }

  public void testBitWithCSVTextInAndObjectArrayOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new Bit("1"));
    dataFormat.setSchema(schema);

    for (String trueBit : new String[] { "true", "TRUE", "1" }) {
      dataFormat.setCSVTextData(trueBit);
      assertTrue((Boolean) dataFormat.getObjectData()[0]);
    }

    for (String falseBit : new String[] { "false", "FALSE", "0" }) {
      dataFormat.setCSVTextData(falseBit);
      assertFalse((Boolean) dataFormat.getObjectData()[0]);
    }
  }

  @Test(expected = SqoopException.class)
  public void testUnsupportedBitWithObjectArrayInAndObjectOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new Bit("1")).addColumn(new Bit("2"));
    dataFormat.setSchema(schema);
    Object[] data = new Object[2];
    data[0] = "1";
    data[1] = "2";
    dataFormat.setObjectData(data);
    assertEquals(true, dataFormat.getObjectData()[0]);
    assertEquals(false, dataFormat.getObjectData()[1]);
  }

  @Test(expected = SqoopException.class)
  public void testUnsupportedBitWithCSVTextInAndObjectOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new Bit("1")).addColumn(new Bit("2"));
    dataFormat.setSchema(schema);
    dataFormat.setCSVTextData("1,3");
    assertEquals(true, dataFormat.getObjectData()[0]);
    assertEquals(false, dataFormat.getObjectData()[1]);
  }

  // **************test cases for arrays*******************
  @Test
  public void testArrayOfStringWithObjectArrayInObjectArrayOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new org.apache.sqoop.schema.type.Array("1", new Text("text")));
    schema.addColumn(new org.apache.sqoop.schema.type.Text("2"));
    dataFormat.setSchema(schema);
    Object[] givenArray = { "A", "B" };
    // create an array inside the object array
    Object[] data = new Object[2];
    data[0] = givenArray;
    data[1] = "text";
    dataFormat.setObjectData(data);
    Object[] expectedArray = (Object[]) dataFormat.getObjectData()[0];
    assertEquals(Arrays.toString(givenArray), Arrays.toString(expectedArray));
    assertEquals("text", dataFormat.getObjectData()[1]);
  }

  @Test
  public void testArrayOfStringWithCSVTextInObjectArrayOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new org.apache.sqoop.schema.type.Array("1", new Text("text")));
    schema.addColumn(new org.apache.sqoop.schema.type.Text("2"));
    dataFormat.setSchema(schema);
    Object[] givenArray = { "A", "B" };
    // create an array inside the object array
    Object[] data = new Object[2];
    data[0] = givenArray;
    data[1] = "text";
    String testData = "'[\"A\",\"B\"]','text'";
    dataFormat.setCSVTextData(testData);
    Object[] expectedArray = (Object[]) dataFormat.getObjectData()[0];
    assertEquals(Arrays.toString(givenArray), Arrays.toString(expectedArray));
    assertEquals("text", dataFormat.getObjectData()[1]);
  }

  @Test
  public void testArrayOfStringWithObjectArrayInCSVTextOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new org.apache.sqoop.schema.type.Array("1", new Text("text")));
    schema.addColumn(new org.apache.sqoop.schema.type.Text("2"));
    dataFormat.setSchema(schema);
    Object[] givenArray = { "A", "B" };
    // create an array inside the object array
    Object[] data = new Object[2];
    data[0] = givenArray;
    data[1] = "text";
    String testData = "'[\"A\",\"B\"]','text'";
    dataFormat.setObjectData(data);
    assertEquals(testData, dataFormat.getCSVTextData());
  }

  @Test
  public void testArrayOfStringWithCSVTextInCSVTextOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new org.apache.sqoop.schema.type.Array("1", new Text("text")));
    schema.addColumn(new org.apache.sqoop.schema.type.Text("2"));
    dataFormat.setSchema(schema);
    String testData = "'[\"A\",\"B\"]','text'";
    dataFormat.setCSVTextData(testData);
    assertEquals(testData, dataFormat.getCSVTextData());
  }

  @Test
  public void testArrayOfComplexStrings() {
    Schema schema = new Schema("test");
    schema.addColumn(new org.apache.sqoop.schema.type.Array("1", new Text("text")));
    schema.addColumn(new org.apache.sqoop.schema.type.Text("2"));
    dataFormat.setSchema(schema);
    Object[] givenArray = { "A''\"ssss", "Bss###''" };
    // create an array inside the object array
    Object[] data = new Object[2];
    data[0] = givenArray;
    data[1] = "tex''t";
    dataFormat.setObjectData(data);
    Object[] expectedArray = (Object[]) dataFormat.getObjectData()[0];
    assertEquals(Arrays.toString(givenArray), Arrays.toString(expectedArray));
    assertEquals("tex''t", dataFormat.getObjectData()[1]);
  }

  @Test
  public void testArrayOfIntegers() {
    Schema schema = new Schema("test");
    schema.addColumn(new org.apache.sqoop.schema.type.Array("1", new FixedPoint("fn")));
    schema.addColumn(new org.apache.sqoop.schema.type.Text("2"));
    dataFormat.setSchema(schema);
    Object[] givenArray = { 1, 2 };
    // create an array inside the object array
    Object[] data = new Object[2];
    data[0] = givenArray;
    data[1] = "text";
    dataFormat.setObjectData(data);
    Object[] expectedArray = (Object[]) dataFormat.getObjectData()[0];
    assertEquals(Arrays.toString(givenArray), Arrays.toString(expectedArray));
    assertEquals("text", dataFormat.getObjectData()[1]);
  }

  @Test
  public void testListOfIntegers() {
    Schema schema = new Schema("test");
    schema.addColumn(new org.apache.sqoop.schema.type.Array("1", new FixedPoint("fn")));
    schema.addColumn(new org.apache.sqoop.schema.type.Text("2"));
    dataFormat.setSchema(schema);
    List<Integer> givenList = new ArrayList<Integer>();
    givenList.add(1);
    givenList.add(1);
    // create an array inside the object array
    Object[] data = new Object[2];
    data[0] = givenList.toArray();
    data[1] = "text";
    dataFormat.setObjectData(data);
    Object[] expectedArray = (Object[]) dataFormat.getObjectData()[0];
    assertEquals(givenList.toString(), Arrays.toString(expectedArray));
    assertEquals("text", dataFormat.getObjectData()[1]);
  }

  public void testSetOfIntegers() {
    Schema schema = new Schema("test");
    schema.addColumn(new org.apache.sqoop.schema.type.Set("1", new FixedPoint("fn")));
    schema.addColumn(new org.apache.sqoop.schema.type.Text("2"));
    dataFormat.setSchema(schema);
    Set<Integer> givenSet = new HashSet<Integer>();
    givenSet.add(1);
    givenSet.add(3);
    // create an array inside the object array
    Object[] data = new Object[2];
    data[0] = givenSet.toArray();
    data[1] = "text";
    dataFormat.setObjectData(data);
    Object[] expectedArray = (Object[]) dataFormat.getObjectData()[0];
    assertEquals(givenSet.toString(), Arrays.toString(expectedArray));
    assertEquals("text", dataFormat.getObjectData()[1]);
  }

  @Test
  public void testArrayOfDecimals() {
    Schema schema = new Schema("test");
    schema.addColumn(new org.apache.sqoop.schema.type.Array("1",
        new org.apache.sqoop.schema.type.Decimal("deci")));
    schema.addColumn(new org.apache.sqoop.schema.type.Text("2"));
    dataFormat.setSchema(schema);
    Object[] givenArray = { 1.22, 2.444 };
    // create an array inside the object array
    Object[] data = new Object[2];
    data[0] = givenArray;
    data[1] = "text";
    dataFormat.setObjectData(data);
    Object[] expectedArray = (Object[]) dataFormat.getObjectData()[0];
    assertEquals(Arrays.toString(givenArray), Arrays.toString(expectedArray));
    assertEquals("text", dataFormat.getObjectData()[1]);
  }

  @Test
  public void testArrayOfObjectsWithObjectArrayInObjectArrayOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new org.apache.sqoop.schema.type.Array("1",
        new org.apache.sqoop.schema.type.Array("array", new FixedPoint("ft"))));
    schema.addColumn(new org.apache.sqoop.schema.type.Text("2"));
    dataFormat.setSchema(schema);
    Object[] givenArrayOne = { 11, 12 };
    Object[] givenArrayTwo = { 14, 15 };

    Object[] arrayOfArrays = new Object[2];
    arrayOfArrays[0] = givenArrayOne;
    arrayOfArrays[1] = givenArrayTwo;

    // create an array inside the object array
    Object[] data = new Object[2];
    data[0] = arrayOfArrays;
    data[1] = "text";
    dataFormat.setObjectData(data);
    Object[] expectedArray = (Object[]) dataFormat.getObjectData()[0];
    assertEquals(2, expectedArray.length);
    assertEquals(Arrays.deepToString(arrayOfArrays), Arrays.deepToString(expectedArray));
    assertEquals("text", dataFormat.getObjectData()[1]);
  }

  @Test
  public void testArrayOfObjectsWithCSVTextInObjectArrayOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new org.apache.sqoop.schema.type.Array("1",
        new org.apache.sqoop.schema.type.Array("array", new FixedPoint("ft"))));
    schema.addColumn(new org.apache.sqoop.schema.type.Text("2"));
    dataFormat.setSchema(schema);
    Object[] givenArrayOne = { 11, 12 };
    Object[] givenArrayTwo = { 14, 15 };

    Object[] arrayOfArrays = new Object[2];
    arrayOfArrays[0] = givenArrayOne;
    arrayOfArrays[1] = givenArrayTwo;

    // create an array inside the object array
    Object[] data = new Object[2];
    data[0] = arrayOfArrays;
    data[1] = "text";
    dataFormat.setCSVTextData("'[\"[11, 12]\",\"[14, 15]\"]','text'");
    Object[] expectedArray = (Object[]) dataFormat.getObjectData()[0];
    assertEquals(2, expectedArray.length);
    assertEquals(Arrays.deepToString(arrayOfArrays), Arrays.deepToString(expectedArray));
    assertEquals("text", dataFormat.getObjectData()[1]);
  }

  @Test
  public void testArrayOfObjectsWithCSVTextInCSVTextOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new org.apache.sqoop.schema.type.Array("1",
        new org.apache.sqoop.schema.type.Array("array", new FixedPoint("ft"))));
    schema.addColumn(new org.apache.sqoop.schema.type.Text("2"));
    dataFormat.setSchema(schema);
    String input = "'[\"[11, 12]\",\"[14, 15]\"]','text'";
    dataFormat.setCSVTextData(input);
    Object[] expectedArray = (Object[]) dataFormat.getObjectData()[0];
    assertEquals(2, expectedArray.length);
    assertEquals(input, dataFormat.getCSVTextData());
  }

  @Test
  public void testArrayOfObjectsWithObjectArrayInCSVTextOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new org.apache.sqoop.schema.type.Array("1",
        new org.apache.sqoop.schema.type.Array("array", new FixedPoint("ft"))));
    schema.addColumn(new org.apache.sqoop.schema.type.Text("2"));
    dataFormat.setSchema(schema);
    Object[] givenArrayOne = { 11, 12 };
    Object[] givenArrayTwo = { 14, 15 };

    Object[] arrayOfArrays = new Object[2];
    arrayOfArrays[0] = givenArrayOne;
    arrayOfArrays[1] = givenArrayTwo;

    // create an array inside the object array
    Object[] data = new Object[2];
    data[0] = arrayOfArrays;
    data[1] = "text";
    dataFormat.setObjectData(data);
    String expected = "'[\"[11, 12]\",\"[14, 15]\"]','text'";
    assertEquals(expected, dataFormat.getCSVTextData());
  }
  //**************test cases for map**********************

  @Test
  public void testMapWithSimpleValueWithObjectArrayInObjectArrayOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new org.apache.sqoop.schema.type.Map("1", new Text("key"), new Text("value")));
    schema.addColumn(new org.apache.sqoop.schema.type.Text("2"));
    dataFormat.setSchema(schema);
    Map<Object, Object> map = new HashMap<Object, Object>();
    map.put("testKey", "testValue");
    // create an array inside the object array
    Object[] data = new Object[2];
    data[0] = map;
    data[1] = "text";
    dataFormat.setObjectData(data);
    @SuppressWarnings("unchecked")
    Map<Object, Object> expectedMap = (Map<Object, Object>) dataFormat.getObjectData()[0];
    assertEquals(map, expectedMap);
    assertEquals("text", dataFormat.getObjectData()[1]);
  }

  @Test
  public void testMapWithComplexIntegerListValueWithObjectArrayInObjectArrayOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new org.apache.sqoop.schema.type.Map("1", new Text("key"), new Array("value",
        new FixedPoint("number"))));
    schema.addColumn(new org.apache.sqoop.schema.type.Text("2"));
    dataFormat.setSchema(schema);
    Map<Object, Object> givenMap = new HashMap<Object, Object>();
    List<Integer> intList = new ArrayList<Integer>();
    intList.add(11);
    intList.add(12);
    givenMap.put("testKey", intList);
    // create an array inside the object array
    Object[] data = new Object[2];
    data[0] = givenMap;
    data[1] = "text";
    dataFormat.setObjectData(data);
    @SuppressWarnings("unchecked")
    Map<Object, Object> expectedMap = (Map<Object, Object>) dataFormat.getObjectData()[0];
    assertEquals(givenMap.toString(), expectedMap.toString());
    assertEquals("text", dataFormat.getObjectData()[1]);
  }

  @Test
  public void testMapWithComplexStringListValueWithObjectArrayInObjectArrayOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new org.apache.sqoop.schema.type.Map("1", new Text("key"), new Array("value",
        new Text("text"))));
    schema.addColumn(new org.apache.sqoop.schema.type.Text("2"));
    dataFormat.setSchema(schema);
    Map<Object, Object> givenMap = new HashMap<Object, Object>();
    List<String> stringList = new ArrayList<String>();
    stringList.add("A");
    stringList.add("A");
    givenMap.put("testKey", stringList);
    // create an array inside the object array
    Object[] data = new Object[2];
    data[0] = givenMap;
    data[1] = "text";
    dataFormat.setObjectData(data);
    @SuppressWarnings("unchecked")
    Map<Object, Object> expectedMap = (Map<Object, Object>) dataFormat.getObjectData()[0];
    assertEquals(givenMap.toString(), expectedMap.toString());
    assertEquals("text", dataFormat.getObjectData()[1]);
  }

  @Test
  public void testMapWithComplexMapValueWithObjectArrayInObjectArrayOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new org.apache.sqoop.schema.type.Map("1", new Text("key"), new Array("value",
        new Text("text"))));
    schema.addColumn(new org.apache.sqoop.schema.type.Text("2"));
    dataFormat.setSchema(schema);
    Map<Object, Object> givenMap = new HashMap<Object, Object>();
    List<String> stringList = new ArrayList<String>();
    stringList.add("A");
    stringList.add("A");
    Map<String, List<String>> anotherMap = new HashMap<String, List<String>>();
    anotherMap.put("anotherKey", stringList);
    givenMap.put("testKey", anotherMap);
    // create an array inside the object array
    Object[] data = new Object[2];
    data[0] = givenMap;
    data[1] = "text";
    dataFormat.setObjectData(data);
    @SuppressWarnings("unchecked")
    Map<Object, Object> expectedMap = (Map<Object, Object>) dataFormat.getObjectData()[0];
    assertEquals(givenMap.toString(), expectedMap.toString());
    assertEquals("text", dataFormat.getObjectData()[1]);
  }

 @Test
  public void testMapWithCSVTextInObjectArrayOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new org.apache.sqoop.schema.type.Map("1", new Text("key"), new Text("value")));
    schema.addColumn(new org.apache.sqoop.schema.type.Text("2"));
    dataFormat.setSchema(schema);
    Map<Object, Object> givenMap = new HashMap<Object, Object>();
    givenMap.put("testKey", "testValue");
    Object[] data = new Object[2];
    data[0] = givenMap;
    data[1] = "text";
    String testData = "'{\"testKey\":\"testValue\"}','text'";
    dataFormat.setCSVTextData(testData);
    @SuppressWarnings("unchecked")
    Map<Object, Object> expectedMap = (Map<Object, Object>) dataFormat.getObjectData()[0];
    assertEquals(givenMap, expectedMap);
    assertEquals("text", dataFormat.getObjectData()[1]);
  }

  @Test
  public void testMapWithComplexValueWithCSVTextInObjectArrayOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new org.apache.sqoop.schema.type.Map("1", new Text("key"), new Text("value")));
    schema.addColumn(new org.apache.sqoop.schema.type.Text("2"));
    dataFormat.setSchema(schema);
    Map<Object, Object> givenMap = new HashMap<Object, Object>();
    givenMap.put("testKey", "testValue");
    Object[] data = new Object[2];
    data[0] = givenMap;
    data[1] = "text";
    String testData = "'{\"testKey\":\"testValue\"}','text'";
    dataFormat.setCSVTextData(testData);
    @SuppressWarnings("unchecked")
    Map<Object, Object> expectedMap = (Map<Object, Object>) dataFormat.getObjectData()[0];
    assertEquals(givenMap, expectedMap);
    assertEquals("text", dataFormat.getObjectData()[1]);
  }

  @Test
  public void testMapWithObjectArrayInCSVTextOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new org.apache.sqoop.schema.type.Map("1", new Text("key"), new Text("value")));
    schema.addColumn(new org.apache.sqoop.schema.type.Text("2"));
    dataFormat.setSchema(schema);
    Map<Object, Object> givenMap = new HashMap<Object, Object>();
    givenMap.put("testKey", "testValue");
    Object[] data = new Object[2];
    data[0] = givenMap;
    data[1] = "text";
    String testData = "'{\"testKey\":\"testValue\"}','text'";
    dataFormat.setObjectData(data);
    assertEquals(testData, dataFormat.getCSVTextData());
  }

  @Test
  public void testMapWithCSVTextInCSVTextOut() {
    Schema schema = new Schema("test");
    schema.addColumn(new org.apache.sqoop.schema.type.Map("1", new Text("key"), new Text("value")));
    schema.addColumn(new org.apache.sqoop.schema.type.Text("2"));
    dataFormat.setSchema(schema);
    String testData = "'{\"testKey\":\"testValue\"}','text'";
    dataFormat.setCSVTextData(testData);
    assertEquals(testData, dataFormat.getCSVTextData());
  }
  //**************test cases for schema*******************
  @Test(expected=SqoopException.class)
  public void testEmptySchema() {
    String testData = "10,34,'54','random data'," + getByteFieldString(new byte[] { (byte) -112, (byte) 54})
        + ",'\\n'";
    Schema schema = new Schema("Test");
    dataFormat.setSchema(schema);
    dataFormat.setCSVTextData(testData);

    @SuppressWarnings("unused")
    Object[] out = dataFormat.getObjectData();
  }

  @Test(expected = SqoopException.class)
  public void testNullSchema() {
    dataFormat.setSchema(null);
    @SuppressWarnings("unused")
    Object[] out = dataFormat.getObjectData();
  }

  @Test(expected = SqoopException.class)
  public void testNotSettingSchema() {
    @SuppressWarnings("unused")
    Object[] out = dataFormat.getObjectData();
  }
}