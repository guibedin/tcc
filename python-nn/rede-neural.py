class RedeNeural:
  """Classe que representa a rede neural utilizada no projeto"""

  x = 1

  def __init__(self):
    self.y = 2

  def f(self, a):
    return a+1



rede = RedeNeural()
print rede.x
print rede.y
print rede.f(15)
