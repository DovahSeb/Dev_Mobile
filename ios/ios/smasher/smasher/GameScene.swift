//
//  GameScene.swift
//  smasher
//
//  Created by etudiant on 17/04/2018.
//  Copyright © 2018 etudiant. All rights reserved.
//

import SpriteKit
import GameplayKit

\\Def des bitmasks
let BallCategory   : UInt32 = 0x1 << 0
let BottomCategory : UInt32 = 0x1 << 1
let BlockCategory  : UInt32 = 0x1 << 2
let PaddleCategory : UInt32 = 0x1 << 3

let GameMessageName = "gameMessage"

class GameScene: SKScene, SKPhysicsContactDelegate {
    
    var ball = SKSpriteNode()
    var paddle = SKSpriteNode()
    var block = SKSpriteNode()
    var scoreLabel = SKLabelNode(fontNamed: "Chalkduster")
    var score = 0
    
    var isFingerOnPaddle = false
    
    lazy var gameState: GKStateMachine = GKStateMachine(states: [
        Tap(scene: self),
        Play(scene: self),
        GameOver(scene: self)])
  
    override func didMove(to view: SKView) {
        super.didMove(to: view)
        
        //Init du sprite de la balle
        ball = self.childNode(withName: "ball") as! SKSpriteNode
        ball.physicsBody!.categoryBitMask = BallCategory
        
        //Ajout de physique sur le bord de l'ecran
        let border = SKPhysicsBody(edgeLoopFrom: self.frame)
        border.friction = 0
        border.restitution = 1
        self.physicsBody = border
        
        //Annule la gravite
        physicsWorld.gravity = CGVector(dx: 0.0, dy: 0.0)
        physicsWorld.contactDelegate = self
        
        //
        let bottomRect = CGRect(x: frame.origin.x, y: frame.origin.y, width: frame.size.width, height: 1)
        let bottom = SKNode()
        bottom.physicsBody = SKPhysicsBody(edgeLoopFrom: bottomRect)
        bottom.physicsBody!.categoryBitMask = BottomCategory
        addChild(bottom)
     
        //
        ball.physicsBody!.contactTestBitMask = BottomCategory | BlockCategory | PaddleCategory
        ball.physicsBody!.collisionBitMask = BlockCategory | PaddleCategory
        
        //Generer une ligne de brique
        let numbricks = 4
        var paddingRow = 0
        for _ in 0..<numbricks{
            let blockTexture = SKTexture(imageNamed: "block")
            let block = SKSpriteNode(texture: blockTexture)
            
            block.physicsBody = SKPhysicsBody(rectangleOf: block.frame.size)
            block.physicsBody!.allowsRotation = false
            block.physicsBody!.friction = 0.0
            block.physicsBody!.affectedByGravity = false
            block.physicsBody!.isDynamic = false
            block.name = "block"
            block.physicsBody!.categoryBitMask = BlockCategory
            block.position = CGPoint(x: (-size.width/2)+150 + CGFloat(paddingRow) , y:size.height/2.4)
            paddingRow = paddingRow + 150
            block.zPosition = 1
            addChild(block)
        }
        
        //Generer une ligne de brique
        let numbricks1 = 4
        var paddingRow1 = 0
        for _ in 0..<numbricks1{
            
            let blockTexture = SKTexture(imageNamed: "block")
            let block = SKSpriteNode(texture: blockTexture)
            
            block.physicsBody = SKPhysicsBody(rectangleOf: block.frame.size)
            block.physicsBody!.allowsRotation = false
            block.physicsBody!.friction = 0.0
            block.physicsBody!.affectedByGravity = false
            block.physicsBody!.isDynamic = false
            block.name = "block"
            block.physicsBody!.categoryBitMask = BlockCategory
            block.position = CGPoint(x: (-size.width/2)+150 + CGFloat(paddingRow1) , y:size.height/3)
            paddingRow1 = paddingRow1 + 150
            block.zPosition = 1
            addChild(block)
            
        }
        
        //Generer une ligne de brique
        let numbricks2 = 4
        var paddingRow2 = 0
        for _ in 0..<numbricks2{
            let blockTexture = SKTexture(imageNamed: "block")
            let block = SKSpriteNode(texture: blockTexture)
            
            block.physicsBody = SKPhysicsBody(rectangleOf: block.frame.size)
            block.physicsBody!.allowsRotation = false
            block.physicsBody!.friction = 0.0
            block.physicsBody!.affectedByGravity = false
            block.physicsBody!.isDynamic = false
            block.name = "block"
            block.physicsBody!.categoryBitMask = BlockCategory
            block.position = CGPoint(x: (-size.width/2)+150 + CGFloat(paddingRow2) , y:size.height/4)
            paddingRow2 = paddingRow2 + 150
            block.zPosition = 1
            addChild(block)
        }
        
        //Affichage du score
        scoreLabel.text = "Score: 0"
        scoreLabel.fontSize = 40
        scoreLabel.fontColor = SKColor.black
        scoreLabel.position = CGPoint(x: -200, y: 100*6)
        addChild(scoreLabel)
       
        //Affichage pour demarrer le jeu
        let gameMessage = SKSpriteNode(imageNamed: "TapToPlay")
        gameMessage.name = GameMessageName
        gameMessage.position = CGPoint(x: frame.midX, y: frame.midY)
        gameMessage.zPosition = 4
        gameMessage.setScale(0.0)
        addChild(gameMessage)
        gameState.enter(Tap.self)
        
    }
    
    
    func touchDown(atPoint pos : CGPoint) {

    }
    
    func touchMoved(toPoint pos : CGPoint) {

    }
    
    func touchUp(atPoint pos : CGPoint) {

    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        switch gameState.currentState {
        case is Tap:
            gameState.enter(Play.self)
            isFingerOnPaddle = true
            
        case is Play:
            let touch = touches.first
            let touchLocation = touch!.location(in: self)
            
            if let body = physicsWorld.body(at: touchLocation) {
                if body.node!.name == "paddle" {
                    isFingerOnPaddle = true
                }
            }
        case is GameOver:
            let newScene = GameScene(fileNamed:"GameScene")
            newScene!.scaleMode = .aspectFit
            let reveal = SKTransition.doorway(withDuration: 0.5)
            self.view?.presentScene(newScene!, transition: reveal)
            
        default:
            break
        }
    }
    
    override func touchesMoved(_ touches: Set<UITouch>, with event: UIEvent?) {
        //Gere le mouvement et la position du paddle
        if isFingerOnPaddle {
            let touch = touches.first
            let touchLocation = touch!.location(in: self)
            let previousLocation = touch!.previousLocation(in: self)
            let paddle = childNode(withName: "paddle") as! SKSpriteNode
            paddle.physicsBody!.categoryBitMask = PaddleCategory
            var paddleX = paddle.position.x + (touchLocation.x - previousLocation.x)
            paddleX = min(paddleX, size.width - paddle.size.width/2)
            paddle.position = CGPoint(x: paddleX, y: paddle.position.y)
        }
       
    }
    
    override func touchesEnded(_ touches: Set<UITouch>, with event: UIEvent?) {
        isFingerOnPaddle = false
    }
    
    override func touchesCancelled(_ touches: Set<UITouch>, with event: UIEvent?) {
        for t in touches { self.touchUp(atPoint: t.location(in: self)) }
    }
    
    
    override func update(_ currentTime: TimeInterval) {
        gameState.update(deltaTime: currentTime)
    }
    
    func didBegin(_ contact: SKPhysicsContact) {
        if gameState.currentState is Play {
        var firstBody: SKPhysicsBody
        var secondBody: SKPhysicsBody
        if contact.bodyA.categoryBitMask < contact.bodyB.categoryBitMask {
            firstBody = contact.bodyA
            secondBody = contact.bodyB
        } else {
            firstBody = contact.bodyB
            secondBody = contact.bodyA
        }
        \\Contact avec le bas de l'ecran: game over
        if firstBody.categoryBitMask == BallCategory && secondBody.categoryBitMask == BottomCategory {
            print("Game Over")
            gameState.enter(GameOver.self)
            gameWon = false
        }
        \\collision avec les briques et augmentation du score
        if firstBody.categoryBitMask == BallCategory && secondBody.categoryBitMask == BlockCategory {
            print("Contact")
            contact.bodyA.node!.removeFromParent()
            score = score + 10
            scoreLabel.text = "Score: " + "\(score)"
            if GameWon() {
                gameState.enter(GameOver.self)
                gameWon = true
            }

        }
    
    }
}
    
    func randomFloat(from: CGFloat, to: CGFloat) -> CGFloat {
        let rand: CGFloat = CGFloat(Float(arc4random())/40)
        return (rand) * (to - from) + from
    }
    
    func GameWon() -> Bool {
        var blocks = 0
        self.enumerateChildNodes(withName: "block") {
            node, stop in
            blocks = blocks + 1
        }
        return blocks == 0
    }
    
    var gameWon : Bool = false {
        didSet {
            let gameOver = childNode(withName: GameMessageName) as! SKSpriteNode
            let textureName = gameWon ? "YouWon" : "GameOver"
            let texture = SKTexture(imageNamed: textureName)
            let actionSequence = SKAction.sequence([SKAction.setTexture(texture),
                                                    SKAction.scale(to: 1.0, duration: 0.25)])
            
            gameOver.run(actionSequence)
        }
    }
}
