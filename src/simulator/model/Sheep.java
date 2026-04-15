package simulator.model;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.model.Animal.Diet;

public class Sheep extends Animal {

	final static String SHEEP_GENETIC_CODE = "Sheep";
	final static double INIT_SIGHT_SHEEP = 40;
	final static double INIT_SPEED_SHEEP = 35;
	final static double BOOST_FACTOR_SHEEP = 2.0;
	final static double MAX_AGE_SHEEP = 8;
	final static double FOOD_DROP_BOOST_FACTOR_SHEEP = 1.2;
	final static double FOOD_DROP_RATE_SHEEP = 20.0;
	final static double DESIRE_THRESHOLD_SHEEP = 65.0;
	final static double DESIRE_INCREASE_RATE_SHEEP = 40.0;
	final static double PREGNANT_PROBABILITY_SHEEP = 0.9;

	private Animal dangerSource;
	private SelectionStrategy dangerStrategy;

	public Sheep(SelectionStrategy mateStrategy, SelectionStrategy dangerStrategy, Vector2D pos) {
		super(SHEEP_GENETIC_CODE, Diet.HERBIVORE, INIT_SIGHT_SHEEP, INIT_SPEED_SHEEP, mateStrategy, pos);
		if (dangerStrategy == null) {
			throw new IllegalArgumentException("Invalid danger strategy. It needs to have value");
		}
		this.dangerStrategy = dangerStrategy;
		// this.dangerSource = null;

	}

	protected Sheep(Sheep p1, Animal p2) {
		super(p1, p2);
		this.dangerStrategy = p1.dangerStrategy;
		this.dangerSource = null;
	}

	@Override
	public State getState() {

		return this.state;
	}

	@Override
	public Vector2D getPosition() {

		return this.pos;
	}

	@Override
	public String getGeneticCode() {

		return this.geneticCode;
	}

	@Override
	public Diet getDiet() {

		return this.diet;
	}

	@Override
	public double getSpeed() {

		return this.speed;
	}

	@Override
	public double getSightRange() {

		return this.sightRange;
	}

	@Override
	public double getEnergy() {

		return this.energy;
	}

	@Override
	public double getAge() {

		return this.age;
	}

	@Override
	public Vector2D getDestination() {

		return this.dest;
	}

	@Override
	public boolean isPregnant() {

		return this.baby != null;
	}

	@Override
	public void update(double dt) {

		/* 1 */ if (!this.state.equals(State.DEAD)) {
			/* 2 */ switch (state) {
			case MATE:
				stateMate(dt);
				break;
			case HUNGER:
				stateHunger(dt);
				break;
			case DANGER:
				stateDanger(dt);
				break;
			default:
				stateNormal(dt);
				break;
			}

			/* 3 */ posIsOut();

			/* 4 */ die();

			/* 5 */ if (this.state != State.DEAD) {
				double food = this.regionMngr.getFood(this, dt);
				this.energy = Utils.constrainValueInRange(this.energy + food, MINIMUM_VALUE, MAX_ENERGY);
			}
		}
	}

	private void posIsOut() {
		double posX = this.pos.getX();
		double posY = this.pos.getY();
		double maxWidth = this.regionMngr.getWidth();
		double maxHeight = this.regionMngr.getHeight();

		if (posX < 0 || posX >= maxWidth || posY < 0 || posY >= maxHeight) {
			while (posX >= maxWidth)
				posX = (posX - maxWidth);
			while (posX < 0)
				posX = (posX + maxWidth);
			while (posY >= maxHeight)
				posY = (posY - maxHeight);
			while (posY < 0)
				posY = (posY + maxHeight);

			this.pos = new Vector2D(posX, posY);
			setState(State.NORMAL);
		}
	}

	private void die() {
		if (this.energy <= MINIMUM_VALUE || this.age > MAX_AGE_SHEEP) {
			setState(State.DEAD);
		}
	}

	@Override
	protected void setNormalStateAction() {

		this.dangerSource = null;
		this.mateTarget = null;
	}

	@Override
	protected void setMateStateAction() {

		this.dangerSource = null;
	}

	@Override
	protected void setHungerStateAction() {

	}

	@Override
	protected void setDangerStateAction() {

		this.mateTarget = null;
	}

	@Override
	protected void setDeadStateAction() {

		this.dangerSource = null;
		this.mateTarget = null;
	}

	private void stateNormal(Double dt) {
		if (this.pos.distanceTo(this.dest) < COLLISION_RANGE) {
			double posX = Utils.RAND.nextDouble() * (this.regionMngr.getWidth() - 1);
			double posY = Utils.RAND.nextDouble() * (this.regionMngr.getHeight() - 1);
			this.dest = new Vector2D(posX, posY);
		}
		this.move(this.speed * dt * Math.exp((this.energy - MAX_ENERGY) * HUNGER_DECAY_EXP_FACTOR));
		this.age += dt;
		this.energy = Utils.constrainValueInRange(this.energy - (FOOD_DROP_RATE_SHEEP * dt), MINIMUM_VALUE, MAX_ENERGY);
		this.desire = Utils.constrainValueInRange(this.desire + (DESIRE_INCREASE_RATE_SHEEP * dt), MINIMUM_VALUE,
				MAX_DESIRE);

		if (this.dangerSource == null) {
			dangerSource = dangerStrategy.select(this,
					regionMngr.getAnimalsInRange(this, (Animal a) -> a.getDiet().equals(Diet.CARNIVORE)));
		}

		if (this.dangerSource != null) {
			setState(State.DANGER);
		} else if (this.dangerSource == null && this.desire > DESIRE_THRESHOLD_SHEEP) {
			setState(State.MATE);
		}
	}

	private void stateDanger(Double dt) {
		/* 1 */
		if (this.dangerSource != null && this.dangerSource.getState() == State.DEAD) {
			dangerSource = null;
		}

		/* 2 */
		if (dangerSource == null) {
			if (this.pos.distanceTo(this.dest) < COLLISION_RANGE) {
				double posX = Utils.RAND.nextDouble() * (this.regionMngr.getWidth() - 1);
				double posY = Utils.RAND.nextDouble() * (this.regionMngr.getHeight() - 1);
				this.dest = new Vector2D(posX, posY);
			}
			this.move(this.speed * dt * Math.exp((this.energy - MAX_ENERGY) * HUNGER_DECAY_EXP_FACTOR));
			this.age += dt;
			this.energy = Utils.constrainValueInRange(this.energy - (FOOD_DROP_RATE_SHEEP * dt), MINIMUM_VALUE,
					MAX_ENERGY);
			this.desire = Utils.constrainValueInRange(this.desire + (DESIRE_INCREASE_RATE_SHEEP * dt), MINIMUM_VALUE,
					MAX_DESIRE);
		} else {
			this.dest = pos.plus(pos.minus(dangerSource.getPosition()).direction());
			this.move(2.0 * speed * dt * Math.exp((energy - MAX_ENERGY) * HUNGER_DECAY_EXP_FACTOR));
			this.age += dt;
			this.energy = Utils.constrainValueInRange(
					(this.energy - (FOOD_DROP_RATE_SHEEP * FOOD_DROP_BOOST_FACTOR_SHEEP * dt)), MINIMUM_VALUE,
					MAX_ENERGY);
			this.desire = Utils.constrainValueInRange(this.desire + (DESIRE_INCREASE_RATE_SHEEP * dt), MINIMUM_VALUE,
					MAX_DESIRE);
		}

		/* 3 */
		if (dangerSource == null || this.pos.distanceTo(this.dangerSource.getPosition()) > this.sightRange) {
			dangerSource = dangerStrategy.select(this,
					regionMngr.getAnimalsInRange(this, (Animal a) -> a.getDiet().equals(Diet.CARNIVORE)));

			if (dangerSource == null) {
				if (this.desire < DESIRE_THRESHOLD_SHEEP) {
					setState(State.NORMAL);
				} else {
					setState(State.MATE);
				}
			}
		}

	}

	private void stateMate(Double dt) {
		if (this.mateTarget != null && (this.mateTarget.getState().equals(State.DEAD)
				|| this.pos.distanceTo(this.mateTarget.getPosition()) > this.sightRange)) {
			this.mateTarget = null;
		}
		if (mateTarget == null) {
			this.mateTarget = this.mateStrategy.select(this,
					regionMngr.getAnimalsInRange(this, (Animal a) -> a.getGeneticCode().equals(this.getGeneticCode())));
			if (this.mateTarget == null) {

				if (this.pos.distanceTo(this.dest) < COLLISION_RANGE) {
					double posX = Utils.RAND.nextDouble() * (this.regionMngr.getWidth() - 1);
					double posY = Utils.RAND.nextDouble() * (this.regionMngr.getHeight() - 1);
					this.dest = new Vector2D(posX, posY);
				}
				this.move(this.speed * dt * Math.exp((this.energy - MAX_ENERGY) * HUNGER_DECAY_EXP_FACTOR));
				this.age += dt;
				this.energy = Utils.constrainValueInRange(this.energy - (FOOD_DROP_RATE_SHEEP * dt), MINIMUM_VALUE,
						MAX_ENERGY);
				this.desire = Utils.constrainValueInRange(this.desire + (DESIRE_INCREASE_RATE_SHEEP * dt),
						MINIMUM_VALUE, MAX_DESIRE);
			}
		} else {
			this.dest = this.mateTarget.getPosition();
			this.move(2.0 * speed * dt * Math.exp((energy - MAX_ENERGY) * HUNGER_DECAY_EXP_FACTOR));
			this.age += dt;
			this.energy = Utils.constrainValueInRange(
					this.energy - (FOOD_DROP_RATE_SHEEP * FOOD_DROP_BOOST_FACTOR_SHEEP * dt), MINIMUM_VALUE,
					MAX_ENERGY);
			this.desire = Utils.constrainValueInRange(this.desire + (DESIRE_INCREASE_RATE_SHEEP * dt), MINIMUM_VALUE,
					MAX_DESIRE);

			if (this.pos.distanceTo(this.mateTarget.getPosition()) < this.sightRange) {
				this.desire = MINIMUM_VALUE;
				this.mateTarget.desire = MINIMUM_VALUE;
				if (!this.isPregnant() && Utils.RAND.nextDouble() < PREGNANT_PROBABILITY_SHEEP) {
					this.baby = new Sheep(this, mateTarget);
				}
				this.mateTarget = null;

			}
		}

		if (this.dangerSource == null) {
			dangerSource = dangerStrategy.select(this,
					regionMngr.getAnimalsInRange(this, (Animal a) -> a.getDiet().equals(Diet.CARNIVORE)));
		}

		if (this.dangerSource != null) {
			setState(State.DANGER);
		} else if (this.desire < DESIRE_THRESHOLD_SHEEP) {
			setState(State.NORMAL);
		}
	}

	private void stateHunger(Double dt) {

	}

}
