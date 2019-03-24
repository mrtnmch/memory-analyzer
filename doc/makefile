TEX=pdflatex

doc: sablona.tex text/main.tex text/zkratky.tex text/literatura.tex
	cd pdf
	./_build.sh
	./_build.sh
	cd ..
	$(TEX) sablona.tex
	$(TEX) sablona.tex
	xdg-open sablona.pdf

open:
	code .

git:
	git add .
	git commit -m "Text update"
	git push