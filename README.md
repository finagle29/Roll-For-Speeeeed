Roll-For-Speeeeed
=================
This is a simulation of a card and dice game.

Rules
=====
pc=player card


set up:

get dealt 2 from pc deck

choose 1 as your pc

roll 4(5)d6 for base speed

roll 5(6)d6 for hp

draw 3(5) for hand


main game loop

roll 2d6 for turn speed

faster player takes turn

slower player takes turn


turn: choose to attack or effect

attack: defender chooses card from attacker's hand

if A, deal 2d6 damage

attacker rolls 1d6 for hit unless A, 2, targeted 10

if 10*roll is greater than attacker's total speed, deal damage based on number

discard played card

draw one


effect: attacker plays one or two cards of the same suit

draw to previous hand size

use effect based on card(s)'(s) suite

discard played card(s)


effect play:

hearts: heal 4, 6

diamonds: each player chooses one card to give to opponent, player chooses two to give to opponent and chooses two from opponent

clubs: deal 4, 6 (shield breaker)

spades: discard 1 and draw 1, draw 1 and discard 1


2s (play based on pc suit):

hearts: both players shuffle hands into deck and draw 4 card hands

diamonds: roll 2d6 and drain the rounded average

clubs: next time opponent deals damage, roll better than 1 to block

spades: roll 1d6 and add to base speed


10s: selfplay always hits, otherwise roll for hit

targeted player sets up but keep hand (draw if hand size increases)


pc factors:

hearts: rolls 6d6 for hp

diamonds: rolls 5d6 for speed

clubs: for each pair in original hand, increase hand size by one

spades: draw 5

kings: can choose to reroll hp, 6s in hand deal damage as As

queens: can choose to reroll hp, base speed

jacks: can choose to reroll base speed, 8s and 9s in hand deal damage as As
