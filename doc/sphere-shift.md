# sixth attempt

Need more basic research.

Insight: Rot('x', x) + Rot('y', y) != Rot('y', y) * Rot('x', x)
So in fact, the traditional code is wrong in that I arbitrarily do x first.

Experiment: only x rotation or only y rotation.
Observations:
1. only turns in expected direction
2. when mouse returns to original point, sphere returns to original position

Idea: do only one rotation!

1. find angle between points (a,b): val theta = Vector3.angle(a,b)
2. find rotation axis: val axis = a cross b
3. Rotate space about the z axis so that the rotation axis lies in the xz plane. 
4. Rotate space about the y axis so that the rotation axis lies along the z axis. 3. rotate so that rotation axis coincides with one of the axis
5. rotate around that axis
6. undo 3 and 4

Idea:
There's a lot of floating-point calculation here which accumulates a lot of imprecision.
During dragging, keep the original matrices R and R1 and calculates the transformed ones
fresh for each point, between mousedown and current point.
After dragging, forget saved original matrices.


# fifth attempt

shift(a: Point, b: Point)

// if a or b are not on sphere, nothing

val av3 = f3dViewSpace(a)	//independent of R, R1
val ar3 = R1 * av3

val (dx,dy) = solution(a,b)

rotate(dx,dy)	//with: R1 = R1.postRot('x', -dx).postRot('y', -dy)
	
val bv3 = f3dViewSpace(b)	//independent of R, R1
val br3 = R1 * bv3		//with changed R1

//want:	ar3 == br3
//Resolve this for dx,dy in terms of a,b.
	ar3 == br3
=
	R1 * av3 == R1.postRot('x', -dx).postRot('y', -dy) * bv3
=
	R1 * av3 == R1 * Rot('x', -dx) * Rot('y', -dy) * bv3
= 		independent of initial rotation (makes sense)
	av3 == Rot('x', -dx) * Rot('y', -dy) * bv3
=
	f3dViewSpace(a) == Rot('x', -dx) * Rot('y', -dy) * f3dViewSpace(b)
=		use factor=0, width=0, height=0 and ignore z
	[a.x, -a.y] == [cosy b.x, sinx siny b.x - cosx b.y]
=
	a.x == cosy b.x
	-a.y == sinx siny b.x - cosx b.y
=
	a.x == cosy b.x
	a.y == cosx b.y - sinx siny b.x
=
	a.x / b.x == cosy
	a.y == cosx b.y - sinx siny b.x
=
	y = acos(a.x / b.x)
	a.y == cosx b.y - sinx siny b.x

