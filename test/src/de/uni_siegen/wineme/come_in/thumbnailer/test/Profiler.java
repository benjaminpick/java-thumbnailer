/*
 * regain - A file search engine providing plenty of formats
 * Copyright (C) 2004  Til Schneider
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Contact: Til Schneider, info@murfman.de
 *
 * CVS information:
 *  $RCSfile$
 *   $Source$
 *     $Date$
 *   $Author$
 * $Revision$
 */
package de.uni_siegen.wineme.come_in.thumbnailer.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Misst die Zeit und den Datendurchsatz für einen Verarbeitungsschritt.
 *
 * @author Til Schneider, www.murfman.de
 */
public class Profiler {

	  /** The logger for this class */
	  private static Logger mLog = Logger.getLogger(Profiler.class);

	  /** Eine Liste mit allen erzeugten Profilern. */
	  private static ArrayList<Profiler> mProfilerList;

	  /** Der Name. */
  private String mName;

  /** Die kummulierte Gesamtzeit. */
  private long mTotalTime;
  /** Die kummulierte Datenmenge. */
  private long mTotalBytes;
  /** Die Anzahl der Messungen. */
  private int mMeasureCount;
  /** Die Anzahl der abgebrochenen Messungen. */
  private int mAbortedMeasureCount;
  /**
   * Die Zeit, zu der die laufende Messung begonnen hat. Ist -1, wenn keine
   * Messung läuft.
   */
  private long mMeasureStart = -1;
  
  private List<String> interim;


  private static volatile Profiler instance = null;
  public static Profiler getInstance()
  {
	  if (instance == null)
		  instance = new Profiler("bla");
	  return instance;
  }
  
  /**
   * Erzeugt eine neue Profiler-Instanz und registriert sie bei der
   * Profiler-Liste.
   *
   * @param name Der Name des Verarbeitungsschrittes, der mit diesem Profiler
   *        gemessen werden sollen.
   */
  public Profiler(String name) {
    mName = name;

    registerProfiler(this);
  }
  
  
  /**
   * Gets the number of measures.
   * 
   * @return The number of measures.
   */
  public int getMeasureCount() {
    return mMeasureCount;
  }
  
  
  /**
   * Gets the number of aborted measures.
   * 
   * @return The number of aborted measures.
   */
  public int getAbortedMeasureCount() {
    return mAbortedMeasureCount;
  }


  /**
   * Gets the current time of the measuring running now.
   * 
   * @return The current measuring time in milli seconds.
   */
  public long getCurrentMeasuringTime() {
    // NOTE: We put the start time in a local variable to avoid it is changed
    //       while this method is executed.
    long startTime = mMeasureStart;
    if (startTime == -1) {
      return -1;
    } else {
      return System.currentTimeMillis() - startTime;
    }
  }


  /**
   * Clears the registered profilers.
   */
  public static synchronized void clearRegisteredProfilers() {
    mProfilerList = null;
  }


  /**
   * Registriert einen Profiler.
   *
   * @param profiler Der zu registrierende Profiler.
   */
  private static synchronized void registerProfiler(Profiler profiler) {
    if (mProfilerList == null) {
      mProfilerList = new ArrayList<Profiler>();
    }

    mProfilerList.add(profiler);
  }


  /**
   * Startet eine Messung.
   */
  public void startMeasuring() {
    if (mMeasureStart != -1) {
      mLog.warn("A profiler measuring for " + mName + " was started, although "
        + "there is currently a measuring running!");
    }
    mMeasureStart = System.currentTimeMillis();
	  interim = new ArrayList<String>();
  }

  public void interimMeasuring(String label)
  {
	  long interimTime = System.currentTimeMillis() - mMeasureStart;
	  interim.add(label + "\t\t\t" + interimTime);
  }

  /**
   * Stoppt eine Messung.
   *
   * @param bytes Die Anzahl der verarbeiteten Bytes.
   */
  public void stopMeasuring(long bytes) {
    if (mMeasureStart == -1) {
      mLog.warn("A profiler measuring for " + mName + " was stopped, although "
        + "there was currently no measuring running!");
    } else {
      mTotalTime += System.currentTimeMillis() - mMeasureStart;
      mTotalBytes += bytes;
      mMeasureCount++;

      mMeasureStart = -1;
    }
  }



  /**
   * Bricht eine Messung ab. Eine Messung wird dann abgebrochen, wenn der
   * Verarbeitungsschritt nicht korrekt verlaufen ist, z.B. weil eine
   * Exception geworfen wurde.
   */
  public void abortMeasuring() {
    if (mMeasureStart == -1) {
      mLog.warn("A profiler measuring for " + mName + " was aborted, although "
        + "there was currently no measuring running!");
    } else {
      mMeasureStart = -1;
      mAbortedMeasureCount++;
    }
  }



  /**
   * Gibt das Resultat der Messungen als String zurück.
   *
   * @return Das Resultat der Messungen
   */
  public String toString() {
	  
	StringBuilder interimStat = new StringBuilder(100);
	interimStat.append("Total: " + mTotalTime + "\n");
	for (String str : interim)
	{
		interimStat.append(str).append("\n");
	}
	return interimStat.toString();
	  /*
    // Get a current snap shot
    long totalTime = mTotalTime;
    long totalBytes = mTotalBytes;
    int measureCount = mMeasureCount;
    int abortedMeasureCount = mAbortedMeasureCount;
    
    // Calculate the results
    long averageTime = 0;
    long averageBytes = 0;
    if (measureCount > 0) {
      averageTime = totalTime / measureCount;
      averageBytes = totalBytes / measureCount;
    }

    long dataRatePerSec = 0;
    double secs = totalTime / 1000.0;
    if (secs > 0) {
      dataRatePerSec = (long) (totalBytes / secs);
    }
    
    double countsPerMinute = 0;
    if (totalTime > 0) {
      countsPerMinute = measureCount * (60d * 1000d) / totalTime;
    }

    // Berechnen, wie gro� die Labels sein m�ssen
    int maxStaticLabelLength = 12;                   // "Average time"
    int maxDynamicLabelLength = 10 + mUnit.length(); // "Completed " + mUnit
    int minLabelLength = Math.max(maxStaticLabelLength, maxDynamicLabelLength);

    // Systemspeziefischen Zeilenumbruch holen
    String lineSeparator = RegainToolkit.getLineSeparator();

    // Statistik ausgeben
    StringBuffer buffer = new StringBuffer(mName + ":");
    NumberFormat integerFormat = NumberFormat.getInstance();
    integerFormat.setMaximumFractionDigits(0);
    if (abortedMeasureCount > 0) {
      buffer.append(lineSeparator);

      appendLabel(buffer, "Aborted " + mUnit, minLabelLength);
      buffer.append(integerFormat.format(abortedMeasureCount) + " " + mUnit + " (");

      // Ausgeben, wieviel % der Messungen fehl schlugen
      int total = abortedMeasureCount + measureCount;
      double errorPercent = (double) abortedMeasureCount / (double) total;
      buffer.append(RegainToolkit.toPercentString(errorPercent));

      buffer.append(")");
    }
    if (measureCount > 0) {
      buffer.append(lineSeparator);

      appendLabel(buffer, "Completed " + mUnit, minLabelLength);
      buffer.append(integerFormat.format(measureCount) + " " + mUnit + lineSeparator);

      appendLabel(buffer, "Total time", minLabelLength);
      buffer.append(RegainToolkit.toTimeString(totalTime) + lineSeparator);

      appendLabel(buffer, "Total data", minLabelLength);
      buffer.append(RegainToolkit.bytesToString(totalBytes) + lineSeparator);

      appendLabel(buffer, "Average time", minLabelLength);
      buffer.append(RegainToolkit.toTimeString(averageTime) + lineSeparator);

      appendLabel(buffer, "Average data", minLabelLength);
      buffer.append(RegainToolkit.bytesToString(averageBytes) + lineSeparator);

      appendLabel(buffer, "Data rate", minLabelLength);
      buffer.append(RegainToolkit.bytesToString(dataRatePerSec) + "/sec" + lineSeparator);

      appendLabel(buffer, "Output", minLabelLength);
      if (countsPerMinute > 10) {
        // No decimals when the count is high
        buffer.append(integerFormat.format(countsPerMinute) + " " + mUnit + "/min");
      } else {
        NumberFormat floatingFormat = NumberFormat.getInstance();
        floatingFormat.setMinimumFractionDigits(2);
        floatingFormat.setMaximumFractionDigits(2);
        buffer.append(floatingFormat.format(countsPerMinute) + " " + mUnit + "/min");
      }
    }

    return buffer.toString(); */
  }

  /**
   * Gibt zurück, ob dieser Profiler genutzt wurde. Das ist der Fall, wenn
   * mindestens eine Messung durchgef�hrt wurde.
   *
   * @return Ob dieser Profiler genutzt wurde.
   */
  public boolean wasUsed() {
    return (mMeasureCount > 0) || (mAbortedMeasureCount > 0);
  }



  /**
   * Gibt die Resultate s�mtlicher genutzter Profiler zurück.
   *
   * @return Die Resultate s�mtlicher genutzter Profiler.
   */
  public static String getProfilerResults() {
    if (mProfilerList == null) {
      return "";
    }
    
    StringBuffer buffer = new StringBuffer();

    for (Profiler profiler : mProfilerList) {
      if (profiler.wasUsed()) {
        buffer.append(profiler);
        buffer.append("\n");
      }
    }

    return buffer.toString();
  }

}
