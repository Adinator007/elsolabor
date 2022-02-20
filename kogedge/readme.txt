README FILE
Keszitette: Tran Adam
Neptun-kod: IUN7OU

Minden kiírt feladatot teljesítettem. A jelenleg a háromszögre fel van textúrázva az aszteroida és ezt tudjuk mozgatni illetve a kattintással a megfelelő helyre vinni.
Ha színt szeretnénk váltani, akkor másik Shader programmal kell dolgoznunk.
Ha az aszteroidával textúrázott háromszöget szeretnénk megtekinteni, akkor a Scene osztályban a 89. sortól az alábbi kódoknak kell lenniük:

val garishProgramUsed = false
gl.useProgram(firstTextureProgram.glProgram)
// gl.useProgram(garishProgram.glProgram)

Fontos, hogy a 3. sor ki legyen kommentezve.

Ha a színváltást szeretnénk megtekinteni, akkor az alábbi kódrészletnek kell szerepelnie ugyanazon a helyen:

val garishProgramUsed = true
// gl.useProgram(firstTextureProgram.glProgram)
gl.useProgram(garishProgram.glProgram)

Ismét fontos a komment. A boolean változó jelzi, hogy a garishProgram nevű program alatt összefogott shaderekkel kell-e dolgozzon a gpu.
Ha igen, akkor a uniformok, és a matrixok ennek megfelelően töltődnek ki az ezt követő sorokban.
Az update végén szerepel a triangleGeometry.draw() hívás, ami kirajzolja a háromszöget feltéve, hogy az látható. Ehhez egy külön flag-et tartok fenn a Scene osztályban.

Kicsit nehezebb volt a kattintások megoldása. Ehhez azt a módszert alkalmaztam, hogy számon tartottam, hogy hova mozdult el eddig a háromszög eredőben (ezt a modelMatrix hajtotta végre a shader-ben) és amikor a felhasználó kattintott, akkor kinulláztam a modelMatrix-ot és értékül adtam neki a mostani kattintás által meghatározott elmozdulás helyét.
