/*

* FFT.java

 *

 * JPick is a free tool for picking first arrivals from SEG-2

 * seismic data.  JPick uses the JFreeChart library from 
http://www.jfree.org/jfreechart/

 */


/**

*

* Copyright 2004, Will Brunner

* will@larkinsoap.com

*

* 117 grove school road

* Brooktondale NY 14817

*

*This file is part of the JPick first arrival picker.


JPick is free software; you can redistribute it and/or modify

it under the terms of the GNU General Public License as published by

the Free Software Foundation; either version 2 of the License, or

(at your option) any later version.


JPick is distributed in the hope that it will be useful,

but WITHOUT ANY WARRANTY; without even the implied warranty of

MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the

GNU General Public License for more details.


You should have received a copy of the GNU General Public License

along with JPick; if not, write to the Free Software

Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

*/

 
package org.myorg.myapi;
 public class FFT {

    // compute the FFT of x[], assuming its length is a power of 2
    public static Complex[] fft(Complex[] x) {
        int N = x.length;
        Complex[] y = new Complex[N];

        
        if (N == 1) {
            y[0] = (Complex)x[0].clone();
            return y;
        }

        // Cooley-Tukey FFT
        Complex[] even = new Complex[N/2];
        Complex[] odd  = new Complex[N/2];
        for (int k = 0; k < N/2; k++) even[k] = x[2*k];
        for (int k = 0; k < N/2; k++) odd[k]  = x[2*k + 1];

        Complex[] q = fft(even);
        Complex[] r = fft(odd);

        for (int k = 0; k < N/2; k++) {
            double kth = -2 * k * Math.PI / N;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k]       = q[k].plus(wk.times(r[k]));
            y[k + N/2] = q[k].minus(wk.times(r[k]));
        }
	
	
        return y;
    }


    // compute the inverse FFT of x[], assuming its length is a power of 2
    public static Complex[] ifft(Complex[] x) {
        int N = x.length;

        // take conjugate
        for (int i = 0; i < N; i++)
            x[i] = x[i].conjugate();

        // compute forward FFT
        Complex[] y = fft(x);

        // take conjugate again
        for (int i = 0; i < N; i++)
            y[i] = y[i].conjugate();

        //normalize by N
        for (int i = 0; i < N; i++)
            y[i] = y[i].times(1.0 /(N));

        return y;

    }
}
