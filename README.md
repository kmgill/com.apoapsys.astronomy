com.apoapsys.astronomy
======================

A Java library of various astronomical algorithms and calculations. This is initially a port of
the code I have written in JavaScript that is in use by my PlanetMaker/Apoapsys Solar suite
of projects, however the long-term goal is to create a general purpose library for the construction
of astronomy-related applications.

## The stuff I've been able to move over are...
* Calculation of a body's position given keplarian elements
> com.apoapsys.astronomy.orbits
* Orbital position calculations for the major bodies of the solar system using VSOP87 tables
> com.apoapsys.astronomy.orbits.vsop87
* Some custom orbital position algorithms, mostly for various moons (some of which originated in Celestia)
> com.apoapsys.astronomy.orbits.custom
* Implementations for Lunar calculations (phases, librations, position, etc) using algorithms from Jean Meeus's "Astronomical Algorithms"
> com.apoapsys.astronomy.moon
* Calculations for determining the position of the Sun
> com.apoapsys.astronomy.sun
* Conversions between astronomical coordinate systems (horizon, equatorial, ecliptic, galactic) (in progress)
> com.apoapsys.astronomy.coords
* Some map projection algorithms I'm taking from an earlier GIS/Elevation Modeling project of mine (Equirectangular, Mercator, Winkel Tripel, etc...)
> com.apoapsys.astronomy.geo
* Math stuff for manipulating values in three-dimensional space (vectors, matricies, euler angles, quaternions, etc...)
> com.apoapsys.astronomy.math

## Some of the other stuff I still need get to
* Satellite positioning and predictions (over-head/bright passes, etc)
* IAU calculations for body rotations
* Calculations for determining transits, occultations, and eclipses
* Physical and observational properties of various solar system bodies (some hard-coded, some via a REST service to an updated datastore I maintain)
* Positions of the stars and other deep-space objects (I know, I've put very little focus beyond the solar system)


Once those are in place, I'll start looking into the real advanced stuff (assuming the stuff I have above is more-or-less the basics). Also,
feel free to contribute code if you feel up to it, as long as I can maintain a trail of ownership (and thus not have to worry about licensing).


**Please Note:** I am a software engineer by trade, and by no means a scientist or astronomer. Most of what is here is self-taught,
and as such, the algorithms are subject to brain-dead mistakes and "F"-ups. If you spot an error, feel free to file an issue or 
contact me at kmsmgill@gmail.com 